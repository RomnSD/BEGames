package com.wskey.game.games.finrace;

import com.wskey.game.Config;
import com.wskey.game.Game;
import com.wskey.game.GameDescriptor;
import com.wskey.game.GameManager;
import com.wskey.game.Match;
import com.wskey.protocol.Frame;
import com.wskey.server.WebSocketClient;


/**
 * @author RomnSD
 */
public class FinRace extends Game
{

    protected int maxMatches = 60;
    protected Config config = new Config();
    
    
    /**
     * @param gameManager GameManager 
     */
    public FinRace(GameManager gameManager)
    {
        super(gameManager, new GameDescriptor(
                "FinRace",                                             // Game name
                "Compite with other players and show is type faster!", // Game description
                "FinRace-Logo.png",                                    // Game logo
                ""
        ), (1000 / 60));
        
        config.setMinTeamsCount(2);
        config.setMaxTeamsCount(2);
        config.setMatchLength((averageFrameRate * (5 * 60)));
        config.setMatchCountdown((averageFrameRate * 5));
        config.lock();
    }
    
    
    /**
     * @return Match 
     */
    public Match generateMatch()
    {
        Match match = new FinRaceMatch(config);
        return (addMatch(match) ? match : null);
    }
    
    
    @Override 
    public boolean addMatch(Match match)
    {
        if (matches.size() >= maxMatches || !(match instanceof FinRaceMatch))
            return false;
        
        return super.addMatch(match);
    }
    
    
    
}
