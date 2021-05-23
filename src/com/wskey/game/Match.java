package com.wskey.game;

import com.wskey.game.entities.Player;
import com.wskey.game.team.Team;
import com.wskey.protocol.Frame;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author RomnSD
 */
public abstract  class Match
{

    protected Config config;
    protected int matchID = COUNTER.getAndIncrement();
    protected MatchStatus matchStatus = MatchStatus.Waiting;
    protected PlayerStatus slotsStatus = PlayerStatus.NeedForPlayers;
    protected HashMap<Integer, Team> teams = new HashMap<>();
    
    public static AtomicInteger COUNTER = new AtomicInteger();

    public enum MatchStatus {
        Waiting,
        Beginning,
        Running
    }

    public enum PlayerStatus {
        NeedForPlayers,
        Full
    }


    /**
     * @param config Config
     */
    public Match(Config config) { this.config = config; }
    
    
    /**
     * @return int 
     */
    public int getMatchID() { return matchID; }


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
    public boolean isEmpty()
    {
        for (Team team : teams.values()) {
            if (team.getSize() != 0) return false;
        }
        return true;
    }


    /**
     * @return boolean
     */
    public boolean canJoin() { return !matchStatus.equals(MatchStatus.Running) && slotsStatus.equals(PlayerStatus.NeedForPlayers); }


    /**
     * @param player Player
     */
    public boolean addPlayer(Player player)
    {
        if (slotsStatus.equals(PlayerStatus.Full))
            return false;

        Team freeTeam= null;

        for (Team team : teams.values()) {
            if (!team.isFull()) {
                freeTeam = team;
                break;
            }
        }

        if (freeTeam == null)
            return false;

        player.setGameSession(freeTeam.openSession(player));
        
        return player.getGameSession() != null;
    }

    
    public abstract void update();
    public abstract void handleFrame(Frame frame, Player player);



}
