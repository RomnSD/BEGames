package com.wskey.game;


/**
 * @author RomnSD
 */
public class Config
{

    protected int minTeamsCount = 0;
    protected int maxTeamsCount = 0;
    protected int playersPerTeam = 0;

    protected int gameTime = 0;
    protected int startingTime = 0;

    protected boolean locked = false;


    public Config() { }

    public int getMinTeamsCount() { return minTeamsCount; }

    public void setMinTeamsCount(int count) { if (!locked) minTeamsCount = count; }

    public int getMaxTeamsCount() { return maxTeamsCount; }

    public void setMaxTeamsCount(int count) { if (!locked) maxTeamsCount = count; }

    public void lock() { locked = true; }


}
