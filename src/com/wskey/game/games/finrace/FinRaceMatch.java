package com.wskey.game.games.finrace;

import com.wskey.game.Config;
import com.wskey.game.Match;
import com.wskey.game.entities.Player;
import com.wskey.game.team.Team;
import com.wskey.protocol.Frame;


/**
 *
 * @author RomnSD
 */
public class FinRaceMatch extends Match
{
    
    
    public FinRaceMatch(Config config)
    {
        super(config);
        for (int i = 0; i < config.getMaxTeamsCount(); i++) {
            Team team = new Team(config.getPlayersPerTeams());
            teams.put(team.getTeamID(), team);
        }
    }
    
    
    @Override
    public void update()
    {
        //...
    }
    
    
    @Override
    public void handleFrame(Frame frame, Player player)
    {
        
    }
}
