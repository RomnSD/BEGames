package com.wskey.game;


/**
 * @author RomnSD
 */
public class GameDescriptor
{
    
    protected String name;
    protected String description;
    protected String icon;
    protected String howToPlay;
    
    public GameDescriptor(String name, String description, String icon, String howToPlay)
    {
        this.name        = name;
        this.description = description;
        this.icon        = icon;
        this.howToPlay   = howToPlay;
    }
    
    
    /**
     * @return String 
     */
    public String getName() { return name; }
    
    
    /**
     * @return String 
     */
    public String getDescription() { return description; }
    
    
    /**
     * @return String 
     */
    public String getIcon() { return icon; }
    
    
    /**
     * @return String 
     */
    public String getHowToPlay() { return howToPlay; }
    
    
}
