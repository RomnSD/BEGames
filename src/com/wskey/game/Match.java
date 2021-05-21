package com.wskey.game;

import com.wskey.game.entities.Player;
import com.wskey.protocol.Frame;

import java.util.LinkedHashMap;

public abstract  class Match
{

    protected int maxPlayers;
    protected int status = STATUS_WAITING;

    protected LinkedHashMap<Integer, Team> teams = new LinkedHashMap<>();

    public static int STATUS_WAITING  = 0x01;
    public static int STATUS_STARTING = 0x02;
    public static int STATUS_RUNNING  = 0x03;

    public int getPlayerCount()
    {
        int count = 0;

        for (Team team : teams.values())
            count += team.members.size();

        return count;
    }

    public boolean canJoin()
    {
        return status != STATUS_RUNNING && getPlayerCount() < maxPlayers;
    }

    public void addPlayer(Player player)
    {
        if (getPlayerCount() >= maxPlayers)
            return;

        Team freeTeam= null;

        for (Team team : teams.values()) {
            ///
        }

        if (freeTeam == null) {
            freeTeam = new Team();
            teams.put(freeTeam.teamID, freeTeam);
        }

        player.setTeam(freeTeam);
        freeTeam.addMember(player);
    }

    public abstract void update();
    public abstract void handleFrame(Frame frame, Player player);



}
