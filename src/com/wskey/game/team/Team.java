package com.wskey.game.team;

import com.wskey.game.Session;
import com.wskey.game.entities.Player;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author RomnSD
 */
public class Team
{

    protected int teamID = COUNTER.getAndIncrement();
    protected HashMap<String, Session> members = new HashMap<>();
    protected int teamSize;

    protected static AtomicInteger COUNTER = new AtomicInteger();


    /**
     * @param size int
     */
    public Team(int size) { teamSize = size; }


    /**
     * Return the current size of the team.
     * @return int
     */
    public int getSize() { return members.size(); }


    /**
     * Return this team identifier.
     * @return int
     */
    public int getTeamID() { return teamID; }


    /**
     * Add a new member to this team, by default status the status will be alive.
     * @param player Player
     * @return       TeamMember
     */
    public Session openSession(Player player) { return openSession(player, Session.Status.Alive); }


    /**
     * @param player Player
     * @param status TeamMember.Status
     * @return       TeamMember
     */
    public Session openSession(Player player, Session.Status status)
    {
        if (members.size() >= teamSize && !status.equals(Session.Status.Spectating))
            return null;

        if (player.getGameSession() != null || members.containsKey(player.getClient().getClientID()))
            return null;

        return members.put(player.getClient().getClientID(), new Session(this, player, status));
    }
    
    
    /**
     * @return boolean
     */
    public boolean isFull() { return getSize() >= teamSize;


    /**
     * @param player String
     * @return       boolean 
     */
    public boolean isMember(String player) { return getMember(player) != null; }


    /**
     * @param player String
     * @return       TeamMember
     */
    public Session getMember(String player) { return members.get(player); }


    /**
     * @param message String
     */
    public void sendMessage(String message)
    {
        consumeAll(member -> {
            member.getPlayer().getClient().sendMessage(message);
        });
    }


    /**
     * @param consumer Consumer<Session>
     */
    public void consumeAll(Consumer<Session> consumer)
    {
        for (Session member : members.values())
            consumer.accept(member);
    }


}
