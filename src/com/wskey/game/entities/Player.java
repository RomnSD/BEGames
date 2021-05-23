package com.wskey.game.entities;

import com.wskey.game.Game;
import com.wskey.game.Session;
import com.wskey.server.WebSocketClient;


/**
 * @author RomnSD
 */
public class Player extends Entity
{

    protected WebSocketClient client;
    protected Game game;
    protected Session gameSession;
    protected boolean closed;


    /**
     * @param client WebSocketClient
     */
    public Player(WebSocketClient client)
    {
        this.client = client;
    }


    /**
     * @return WebSocketClient
     */
    public WebSocketClient getClient() { return client; }
    
    
    /**
     * @return boolean 
     */
    public boolean isLogged() { return client.getUsername() != null; }


    /**
     * @param game Game
     */
    public void setGame(Game game)
    {
        //if (game == null && this.game != null)
            //this.game.
        this.game = game;
    }


    /**
     * @return Game
     */
    public Game getGame() { return game; }


    /**
     * @param team Team
     */
    public void setGameSession(Session session) { gameSession = session; }


    /**
     * @return Team
     */
    public Session getGameSession() { return gameSession; }


    /**
     * @return boolean
     */
    public boolean isClosed() { return closed; }


    public void close()
    {
        closed = true;
    }


}
