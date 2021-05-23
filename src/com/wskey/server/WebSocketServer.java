package com.wskey.server;

import com.wskey.protocol.Frame;
import com.wskey.protocol.types.CloseFrame;
import com.wskey.protocol.types.TextFrame;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.security.MessageDigest;


/**
 * @author RomnSD
 */
public class WebSocketServer implements Runnable
{

    protected SocketAddress address;
    protected ServerSocketChannel socketChannel;

    protected List<String> acceptedHosts;
    protected HashMap<String, WebSocketEndpoint> endpoints = new HashMap<>();
    protected ConcurrentHashMap<String, WebSocketClient> clients = new ConcurrentHashMap<>();
    protected HashMap<String, Integer> connections = new HashMap<>();
    protected HashMap<String, Long> blockedAddresses = new HashMap<>();

    protected Thread thread;
    protected boolean closed = false;

    protected static final String SERVER_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";


    /**
     * @param host String
     * @param port int
     */
    public WebSocketServer(String host, int port)
    {
        address = new InetSocketAddress(host, port);
    }


    /**
     * @param hosts LinkedList<String>
     */
    public void setAcceptedHosts(List<String> hosts) { acceptedHosts = hosts; }


    /**
     * @return LinkedList<String>
     */
    public List<String> getAcceptedHosts() { return acceptedHosts; }


    /**
     * Register a new endpoint.
     * @param endpoint WebSocketEndpoint
     */
    public void addEndpoint(WebSocketEndpoint endpoint)
    {
        endpoint.setServer(this);
        endpoints.put(endpoint.getRoute(), endpoint);
    }


    /**
     * Get a registered endpoint.
     * @param channel String
     * @return        WebSocketEndpoint
     */
    public WebSocketEndpoint getEndpoint(String channel) { return endpoints.get(channel); }


    /**
     * @param channel String
     */
    public void removeEndpoint(String channel) { endpoints.remove(channel); }


    /**
     * Starts the server.
     */
    public void start()
    {
        if (thread != null)
            return;

        thread = new Thread(this, "WebSocketServer");
        thread.setDaemon(false);
        thread.start();
    }


    /**
     * Runs the server.
     */
    public void run()
    {
        if (socketChannel != null || closed)
            return;

        try {
            socketChannel = ServerSocketChannel.open();
            socketChannel.bind(this.address);
            socketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Starting WebSocket Server: " + address.toString());

            while (!closed) {
                int count = selector.select();

                if (count == 0)
                    continue;

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (!key.isValid())
                        continue;

                    if (key.isAcceptable()) {
                        SocketChannel socket = socketChannel.accept();
                        socket.configureBlocking(false);

                        socket.register(selector, SelectionKey.OP_READ);
                        continue;
                    }

                    if (key.isReadable()) {
                        if (!this.handleRequest(key)) closeClient(key);
                    }
                }
            }

        } catch (Throwable exception) {
            exception.printStackTrace();
        }

        Iterator<WebSocketClient> iterator = clients.values().iterator();
        
        while(iterator.hasNext())
            iterator.next().sendClose(CloseFrame.SHUTDOWN, "See you later!");
    }


    /**
     * Handle every request coming from clients.
     * If something goes wrong while decoding the request then the client will be disconnected.
     * @param key SelectionKey
     * @return    boolean
     */
    public boolean handleRequest(SelectionKey key)
    {
        try {
            SocketChannel   channel = (SocketChannel) key.channel();
            WebSocketClient client  = clients.get(channel.getRemoteAddress().toString());
            
            if (client == null) {
                String address = channel.getRemoteAddress().toString().split(":")[0];
                
                if (isBlocked(address) || checkForBots(address))
                    return false;
            }
            
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            if (channel.read(buffer) <= 0)
                return false;

            if (client == null)
                return acceptNewClient(channel, new String(buffer.array()));

            buffer.position(0);

            Frame frame = Frame.decodeRawFrame(buffer);

            if (frame == null) {
                client.sendClose(CloseFrame.UNEXPECTED_BEHAVIOR, "What is it?");
                return false;
            }
            
            client.handleFrame(frame);
            
            return true;
        } catch (Throwable exception) { exception.printStackTrace(); }

        return false;
    }


    /**
     * @param key SelectionKey
     */
    protected void closeClient(SelectionKey key)
    {
        try {
            key.cancel();
            key.channel().close();
        } catch (Throwable exception) { exception.printStackTrace(); }
    }


    /**
     * @param client WebSocketClient client
     */
    public void closeClient(WebSocketClient client)
    {
        try {
            clients.remove(client.getSocketChannel().getRemoteAddress().toString());
            client.getSocketChannel().close();
        } catch (Throwable exception) { exception.printStackTrace(); }
    }
    
    
    /**
     * @param ip     String 
     * @param ms     long 
     * @param reason String 
     */
    public void blockIP(String ip, long ms, String reason)
    {
        blockedAddresses.put(ip, ms);
        System.out.println("Address " + ip + " has been blocked for " + ms + "ms, reason: " + reason);
    }
    
    
    /**
     * @param ip String 
     * @return   boolean 
     */
    public boolean isBlocked(String ip)
    {
        long time = blockedAddresses.getOrDefault(ip, 0L);
        
        if (time == 0L)
            return false;
    
        if ((time - System.currentTimeMillis()) > 0L)
            return true;
        
        blockedAddresses.remove(ip);
        return false;
    }
    
    
    public boolean checkForBots(String ip)
    {
        int count = connections.getOrDefault(ip, 0) + 1;
        
        if (count >= 15) {
            // Maybe instead of blocking the IP i could cancel new connections?
            blockIP(ip, (System.currentTimeMillis() + 600000), "Too many connections using the same IP.");
            return true;
        }
        
        connections.put(ip, count);
        return false;
    }


    /**
     * @param socket  SocketChannel
     * @param request String
     * @return        boolean
     */
    protected boolean acceptNewClient(SocketChannel socket, String request)
    {
        HashMap<String, String> headers = WebSocketServer.parseRawRequest(request);

        if (!headers.get("Connection").equals("Upgrade") && !headers.get("Upgrade").equals("websocket"))
            return false;

        if (acceptedHosts != null && !acceptedHosts.contains(headers.get("Origin")))
            return false;

        WebSocketEndpoint endpoint = endpoints.get(headers.get("Method").split(" ")[1]);

        if (endpoint == null)
            return false;

        try {
            String token = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((headers.get("Sec-WebSocket-Key") + SERVER_GUID).getBytes(StandardCharsets.UTF_8)));

            String response = "HTTP/1.1 101 Switching Protocols\r\n" +
                              "Upgrade: websocket\r\n"               +
                              "Connection: Upgrade\r\n"              +
                              "Sec-WebSocket-Accept: " + token + "\r\n\r\n";

            WebSocketClient client = new WebSocketClient(socket, endpoint, WebSocketClient.randomClientID());

            if (!endpoint.onLogin(client))
                return false;

            socket.write(ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8)));

            clients.put(socket.getRemoteAddress().toString(), client);

            return true;
        } catch (Throwable e) { e.printStackTrace(); }

        return false;
    }


    /**
     * @param request String
     * @return        HashMap<String, String>
     */
    protected static HashMap<String, String> parseRawRequest(String request)
    {
        HashMap<String, String> headers = new HashMap<>();

        if (request.isEmpty())
            return headers;

        List<String> entries = List.of(request.split("\r\n"));

        if (entries.isEmpty())
            return headers;

        headers.put("Method", entries.get(0));

        int lastIndex = (entries.size() - 1);

        for (int i = 1; i < lastIndex; i++) {
            String header = entries.get(i);

            if (!header.contains(": "))
                continue;

            String[] parts = header.split(": ");

            if (parts.length != 2)
                continue;

            headers.put(parts[0], parts[1]);
        }

        return headers;
    }
    
    
    /**
     * Return a read-only map of clients.
     * @return Map<String, WebSocketClient>
     */
    public Map<String, WebSocketClient> getClients() { return clients; } 
    
    
    /**
     * @param consumer Consumer<WebSocketClient>
     */
    public void consumeAll(Consumer<WebSocketClient> consumer)
    {
        Iterator<WebSocketClient> iterator = clients.values().iterator();
        
        while (iterator.hasNext())
            consumer.accept(iterator.next());
    }


    /**
     * Check if the server is still running.
     * @return boolean
     */
    public boolean isClosed() { return closed; }


    public void close()
    {
        if (thread == null || closed)
            return;

        try {
            closed = true;
            thread.interrupt();
            thread.join();
        } catch (Throwable exception) { exception.printStackTrace(); }
    }


}
