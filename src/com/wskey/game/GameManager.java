package com.wskey.game;

import com.google.gson.Gson;
import com.wskey.game.entities.Player;
import com.wskey.protocol.Frame;
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

        Player player = players.get(client.getClientID());

        if (player == null)
            return;
    }


    @Override
    public void onQuit(WebSocketClient client)
    {
        Player player = players.get(client.getClientID());

        if (player == null)
            return;

        player.close();
        players.remove(client.getClientID());
    }


}
