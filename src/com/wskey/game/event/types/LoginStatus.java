package com.wskey.game.event.types;


import com.google.gson.JsonObject;
import com.wskey.game.event.Event;


/**
 *
 * @author RomnS
 */
public class LoginStatus extends Event
{
    
    public LoginStatus(int code)
    {
        super("loginState", new JsonObject());
        data.addProperty("code", code);
    }
    
    
    public static LoginStatus success() { return new LoginStatus(1024); }
    
    public static LoginStatus error() { return new LoginStatus(1020); } 
    
    
}
