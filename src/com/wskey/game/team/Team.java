package com.wskey.game.team;

import com.wskey.game.entities.Player;

import java.util.HashMap;
import java.util.function.Consumer;


/**
 * @author RomnSD
 */
public class Team
{

    protected Integer teamID = COUNTER++;
    protected HashMap<String, TeamMember> members = new HashMap<>();
    protected int teamSize;

    protected static int COUNTER = 0;


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
    public TeamMember addMember(Player player) { return addMember(player, TeamMember.Status.Alive); }


    /**
     * @param player Player
     * @param status TeamMember.Status
     * @return       TeamMember
     */
    public TeamMember addMember(Player player, TeamMember.Status status)
    {
        if (members.size() >= teamSize && !status.equals(TeamMember.Status.Spectating))
            return null;

        if (player.getTeam() != null || members.containsKey(player.getClient().getClientID()))
            return null;

        return members.put(player.getClient().getClientID(), new TeamMember(this, player, status));
    }


    /**
     * @param player String
     * @return       boolean
     */
    public boolean isMember(String player) { return getMember(player) != null; }


    /**
     * @param player String
     * @return       TeamMember
     */
    public TeamMember getMember(String player) { return members.get(player); }


    /**
     * @param message String
     */
    public void sendMessage(String message)
    {
        consumeAll(member -> {
            member.getPlayer().getClient().sendMessage(message);
        });
    }


    public void consumeAll(Consumer<TeamMember> consumer)
    {
        for (TeamMember member : members.values())
            consumer.accept(member);
    }


}
