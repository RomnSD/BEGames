package com.wskey.game;


/**
 * @author RomnSD
 */
public class Config
{

    protected int minTeamsCount = 0;
    protected int maxTeamsCount = 0;
    protected int playersPerTeam = 0;

    protected int matchLength = 0;
    protected int matchCountdown = 0;

    protected boolean locked = false;


    public Config() { }

    
    public int getMinTeamsCount() { return minTeamsCount; }

    
    public void setMinTeamsCount(int count) { if (!locked) minTeamsCount = count; }

    
    public int getMaxTeamsCount() { return maxTeamsCount; }

    
    public void setMaxTeamsCount(int count) { if (!locked) maxTeamsCount = count; }
    
    public int getPlayersPerTeams() { return playersPerTeam; }
    
    public void setPlayersPerTeams(int amount) { playersPerTeam = amount; }
    
    public int getMatchLength() { return matchLength; }
    
    public void setMatchLength(int length) { matchLength = length; }
    
    public int getMatchCountdown() { return matchCountdown; }
    
    public void setMatchCountdown(int countdown) { matchCountdown = countdown; }

    
    public void lock() { locked = true; }


}
