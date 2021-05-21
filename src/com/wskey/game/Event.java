package com.wskey.game;

import com.wskey.game.entities.Player;

public class Event
{

    public String name;
    public String data;
    public Player sender;

    public Event(String name, String data)
    {
        this.name = name;
        this.data = data;
    }




}
