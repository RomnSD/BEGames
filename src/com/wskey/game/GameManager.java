package com.wskey.game;

import com.google.gson.Gson;
import com.wskey.game.entities.Player;
import com.wskey.game.event.Event;
import com.wskey.game.event.types.LoginStatus;
import com.wskey.protocol.Frame;
import com.wskey.protocol.types.CloseFrame;
import com.wskey.protocol.types.TextFrame;
import com.wskey.server.WebSocketClient;
import com.wskey.server.WebSocketEndpoint;
import com.wskey.server.WebSocketServer;

import java.util.HashMap;


/**
 * @author RomnSD
 */
public class GameManager extends WebSocketEndpoint
{

    protected WebSocketServer server;
    protected HashMap<String, Game> games = new HashMap<>();
    protected HashMap<String, Player> players = new HashMap<>();

    public static Gson GSON = new Gson();


    /**
     * @param server WebSocketServer
     */
    public GameManager(WebSocketServer server, String channel)
    {
        super(channel);
        this.server = server;
        this.server.addEndpoint(this);
    }


    /**
     * @return WebSocketServer
     */
    public WebSocketServer getServer() { return server; }


    /**
     * @param game Game
     */
    public void addGame(Game game)
    {
        if (games.containsKey(game.getClass().getName()))
            return; // TODO: Add exception

        games.putIfAbsent(game.getClass().getName(), game);
    }


    /**
     * @param game String
     */
    public void removeGame(String game)
    {
        games.remove(game);
        server.removeEndpoint(game);
    }


    /**
     * @param player Player
     */
    public void addPlayer(Player player)
    {
        players.putIfAbsent(player.getClient().getClientID(), player);
    }


    /**
     * @param player String
     * @return       Player
     */
    public Player getPlayer(String player) { return players.get(player); }


    /**
     * @param player String
     */
    public void removePlayer(String player) { players.remove(player); }


    @Override
    public boolean onLogin(WebSocketClient client)
    {
        players.put(client.getClientID(), new Player(client));
        return true;
    }


    @Override
    public void onData(Frame frame, WebSocketClient client)
    {
        if (!(frame instanceof TextFrame))
            return;
        
        frame.decode();

        Player player = players.get(client.getClientID());

        if (player == null)
            return;

        Event event;
        
        try { event = GSON.fromJson(((TextFrame) frame).data, Event.class); } catch (Throwable exception) {
            System.out.println("Exception while handling frame: " + ((TextFrame) frame).data);
            exception.printStackTrace();
            return;
        }
        
        if (event.name == null || event.data == null)
            return;
        
        if (!player.isLogged()) {
            if (!event.name.equals("login"))
                return;
            
            String username = event.data.get("username").getAsString().toLowerCase();
            
            if (username.isBlank() || username.isEmpty() || username.length() < 5 || username.length() > 15) {
                client.sendMessage(LoginStatus.error().json());
                return;
            }
            
            for (WebSocketClient user : server.getClients().values()) {
                if (username.equals(user.getUsername())) {
                    client.sendMessage(LoginStatus.error().json());
                    return;
                }
            }
            
            System.out.println(username + " has logged in!");
            
            player.getClient().setUsername(username);
            player.getClient().sendMessage(LoginStatus.success().json());
            return;
        }
        
        switch (event.name) {
            
            case "gamelist":
                if (player.getGame() != null)
                    return;
                
                player.getClient().sendMessage(""); // game list 
                return;
                
            case "connect":
                Game game = games.get(event.data);
                
                if (game == null) {
                    client.sendClose(CloseFrame.UNEXPECTED_BEHAVIOR, "");
                    return;
                }
                
                player.setGame(game);
                player.getClient().sendMessage(""); // success 
                return;
               
        }
    }


    @Override
    public void onQuit(WebSocketClient client)
    {
        Player player = players.get(client.getClientID());

        if (player == null)
            return;
        
        System.out.println(player.getClient().getUsername() + " has left!");

        player.close();
        players.remove(client.getClientID());
    }


}
