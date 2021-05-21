package com.wskey.game;

import com.wskey.game.entities.Player;

import java.util.HashMap;

public class Team
{

    protected Integer teamID;
    protected HashMap<String, Player> members = new HashMap<>();

    protected static int COUNTER = 0;

    public Team()
    {
        teamID = COUNTER++;
    }

    public Integer getTeamID() { return teamID; }

    public void addMember(Player player)
    {
        members.put(player.getClient().getClientID(), player);
    }

    public boolean isMember(String player) { return getMember(player) != null; }

    public Player getMember(String player) { return members.get(player); }


}
