package com.wskey.game.entities;

import com.wskey.game.Game;
import com.wskey.game.Team;
import com.wskey.protocol.Frame;
import com.wskey.server.WebSocketClient;

public class Player extends Entity
{

    protected WebSocketClient client;
    protected Game game;
    protected Team team;
    protected boolean closed;


    public Player(WebSocketClient client)
    {
        this.client = client;
    }

    public WebSocketClient getClient()
    {
        return client;
    }

    public void setGame(Game game) { this.game = game; }

    public Game getGame() { return game; }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void handleFrame(Frame frame)
    {
        //logged
    }


    public boolean isClosed() {
        return closed;
    }

    public void close()
    {
        closed = true;
    }


}
