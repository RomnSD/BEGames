package com.wskey.game;

import com.wskey.game.entities.Player;
import com.wskey.game.team.Team;


/**
 * @author RomnSD
 */
public class Session
{

    protected Team team;
    protected Player player;
    protected Status status;

    public enum Status {
        Spectating,
        Alive,
        Death
    }


    /**
     * @param team   Team
     * @param player Player
     * @param status Status
     */
    public Session(Team team, Player player, Status status)
    {
        this.team   = team;
        this.player = player;
        this.status = status;
    }
    
    
    /**
     * Return this session current team
     * @return 
     */
    public Team getTeam() { return team; } 


    /**
     * @return Player
     */
    public Player getPlayer() { return player; }


    /**
     * @return Status
     */
    public Status getStatus() { return status; }


    /**
     * @param status Status
     */
    public void setStatus(Status status) { this.status = status; }



}
