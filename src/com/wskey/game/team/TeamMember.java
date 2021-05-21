package com.wskey.game.team;

import com.wskey.game.entities.Player;


/**
 * @author RomnSD
 */
public class TeamMember
{

    protected Team team;
    protected Player player;
    protected Status status;

    enum Status {
        Spectating,
        Alive,
        Death
    }


    /**
     * @param team   Team
     * @param player Player
     * @param status Status
     */
    public TeamMember(Team team, Player player, Status status)
    {
        this.team   = team;
        this.player = player;
        this.status = status;
    }


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
