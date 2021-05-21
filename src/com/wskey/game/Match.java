package com.wskey.game;

import com.wskey.game.entities.Player;
import com.wskey.game.team.Team;
import com.wskey.protocol.Frame;

import java.util.HashMap;


/**
 * @author RomnSD
 */
public abstract  class Match
{

    protected Config config;
    protected MatchStatus matchStatus = MatchStatus.Waiting;
    protected PlayerStatus slotsStatus = PlayerStatus.NeedForPlayers;
    protected HashMap<Integer, Team> teams = new HashMap<>();

    enum MatchStatus {
        Waiting,
        Beginning,
        Running
    }

    enum PlayerStatus {
        NeedForPlayers,
        Full
    }


    /**
     * @param config Config
     */
    public Match(Config config) { this.config = config; }


    /**
     * @return Config
     */
    public Config getConfig() { return config; }


    /**
     * @return int
     */
    public int getPlayerCount()
    {
        int count = 0;

        for (Team team : teams.values())
            count += team.getSize();

        return count;
    }


    /**
     * @return boolean
     */
    public boolean canJoin() { return !matchStatus.equals(MatchStatus.Running) && slotsStatus.equals(PlayerStatus.NeedForPlayers); }


    /**
     * @param player Player
     */
    public void addPlayer(Player player)
    {
        if (slotsStatus.equals(PlayerStatus.Full))
            return;

        Team freeTeam= null;

        for (Team team : teams.values()) {
            //
        }

        if (freeTeam == null) {
            freeTeam = new Team(config.playersPerTeam);
            //teams.put(freeTeam.teamID, freeTeam);
        }

        player.setTeam(freeTeam);
        freeTeam.addMember(player);
    }

    public abstract void update();
    public abstract void handleFrame(Frame frame, Player player);



}
