package com.wskey.game.entities;

import com.wskey.game.Game;
import com.wskey.game.team.Team;
import com.wskey.server.WebSocketClient;


/**
 * @author RomnSD
 */
public class Player extends Entity
{

    protected WebSocketClient client;
    protected Game game;
    protected Team team;

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
    public WebSocketClient getClient()
    {
        return client;
    }


    /**
     * @param game Game
     */
    public void setGame(Game game) { this.game = game; }


    /**
     * @return Game
     */
    public Game getGame() { return game; }


    /**
     * @param team Team
     */
    public void setTeam(Team team) {
        this.team = team;
    }


    /**
     * @return Team
     */
    public Team getTeam() {
        return team;
    }


    /**
     * @return boolean
     */
    public boolean isClosed() {
        return closed;
    }


    public void close()
    {
        closed = true;
    }


}
