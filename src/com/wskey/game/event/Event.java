package com.wskey.game.event;


import com.google.gson.JsonObject;
import com.wskey.game.GameManager;


/**
 * @author RomnSD
 */
public class Event
{

    public String name;
    public JsonObject data;
    
    
    public Event(String name, JsonObject data)
    {
        this.name = name;
        this.data = data;
    }

    
    /**
     * @param name String
     * @param data String
     */
    public Event(String name, String data)
    {
        this.name = name;
        this.data = GameManager.GSON.fromJson(data, JsonObject.class);
    }
    
    
    /**
     * @return String 
     */
    public String json()
    {
        data.addProperty("name", name);
        return GameManager.GSON.toJson(data);
    }
    
    
}
