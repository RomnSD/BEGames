package com.wskey.server;

import com.wskey.protocol.Frame;
import com.wskey.protocol.types.*;

import java.nio.channels.SocketChannel;
import java.util.Random;


/**
 * @author RomnSD
 */
public class WebSocketClient
{

    protected SocketChannel socket;
    protected WebSocketEndpoint endpoint;
    protected String clientID;
    protected String username;

    protected long pingTime = 0L;
    protected boolean closed = false;
    
    protected int messagesPerSecond = 0;
    protected long lastSpamCheck = 0;

    protected static int COUNTER = 0;
    protected static Random RANDOM = new Random();


    /**
     * @param socket   SocketChanel
     * @param endpoint WebSocketEndpoint
     */
    public WebSocketClient(SocketChannel socket, WebSocketEndpoint endpoint, String clientID)
    {
        this.socket   = socket;
        this.endpoint = endpoint;
        this.clientID = clientID;
    }


    /**
     * @return SocketChannel
     */
    public SocketChannel getSocketChannel() { return socket; }


    /**
     * @return WebSocketEndpoint
     */
    public WebSocketEndpoint getEndpoint() { return endpoint; }


    /**
     * @return String 
     */
    public String getClientID() { return clientID; }
    
    
    /**
     * @return String 
     */
    public String getUsername() { return username; }
    
    
    /**
     * @param username String 
     */
    public void setUsername(String username) { this.username = username; }


    /**
     * @param message String
     */
    public void sendMessage(String message) { sendMessage(new TextFrame(message)); }


    public void sendPing()
    {
        if (pingTime != 0L || closed)
            return;

        sendMessage(new PingFrame());
        pingTime = System.currentTimeMillis();
    }


    /**
     * @param code    short
     * @param message String 
     */
    public void sendClose(short code, String message)
    {
        sendMessage(new CloseFrame(code, message));
        close();
    }


    /**
     * @param frame Frame
     */
    public void sendMessage(Frame frame)
    {
        if (closed)
            return;

        try { socket.write(frame.encode()); } catch (Throwable exception) {
            exception.printStackTrace();
        }
    }


    /**
     * @param frame Frame
     */
    public void handleFrame(Frame frame)
    {
        if (closed || isSpam())
            return;

        if (frame instanceof PingFrame)
            sendMessage(new PongFrame());

        if (frame instanceof PongFrame) {
            if (pingTime == 0L)
                return;

            System.out.println("Client took " + (System.currentTimeMillis() - pingTime) + "ms to respond.");

            pingTime = 0L;
        }

        if (frame instanceof CloseFrame) {
            close();
            return;
        }

        endpoint.onData(frame, this);
    }
    
    
    /**
     * @return boolean
     */
    public boolean isSpam()
    {
        long now = System.currentTimeMillis();
        
        if ((now - lastSpamCheck) >= 1000) {
            lastSpamCheck = now;
            messagesPerSecond = 0;
            return false;
        }
        
        messagesPerSecond += 1;
        
        if (messagesPerSecond > 60) {
            endpoint.getServer().blockIP(socket.getRemoteAddress().toString().split(":")[0], (now + 600000), "Spam " + messagesPerSecond);
            close();
            return true;
        }
        
        return false;
    }


    /**
     * @return String
     */
    public static String randomClientID()
    {
        char[] output = new char[5];

        for (int i = 0; i < 5; i++)
            output[i] = (char) (RANDOM.nextInt(5) + 'a');

        return new String(output) + (COUNTER++);
    }


    /**
     * @return boolean
     */
    public boolean isClosed() { return closed; }


    public void close()
    {
        closed = true;
        endpoint.onQuit(this);
        endpoint.getServer().closeClient(this);
    }


}
