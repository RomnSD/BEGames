package com.wskey.game;

import com.wskey.game.entities.Player;
import com.wskey.game.event.Event;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * @author RomnSD
 */
public abstract class Game extends TimerTask
{

    protected GameManager gameManager;
    protected GameDescriptor gameDescriptor;
    protected int averageFrameRate;
    protected Timer timer = new Timer();
    protected HashMap<Integer, Match> matches = new HashMap<>();
    protected ConcurrentLinkedDeque<Player> deque = new ConcurrentLinkedDeque<>();


    /**
     * @param gameManager GameManager
     */
    public Game(GameManager gameManager, GameDescriptor gameDescriptor, int averageFrameRate)
    {
        this.gameManager      = gameManager;
        this.gameDescriptor   = gameDescriptor;
        this.averageFrameRate = averageFrameRate;
        timer.schedule(this, 0, averageFrameRate);
    }
    
    
    /**
     * @return GameManager
     */
    public GameManager getGameManager() { return gameManager; }
    
    
    /**
     * @return GameDescriptor
     */
    public GameDescriptor getGameDescriptor() { return gameDescriptor; }
    
    
    /**
     * @param match Match 
     * @return      boolean 
     */
    public boolean addMatch(Match match) { return matches.putIfAbsent(match.getMatchID(), match) == match; }


    /**
     * @return Timer
     */
    public Timer getTimer() { return timer; }


    public void run()
    {
        updateQueue();
        updateMatches();
    }


    protected void updateQueue()
    {
        if (deque.isEmpty()) {
            Match freeMatch = null;

            for (Match match : matches.values()) {
                if (match.canJoin()) {
                    freeMatch = match;
                    break;
                }
            }

            if (freeMatch != null) {
                while (freeMatch.canJoin()) {
                    Player player = deque.removeFirst();

                    if (player.isClosed() || player.getGame() != null)
                        continue;

                    freeMatch.addPlayer(player);
                    deque.addLast(player);
                }
            }
        }
    }
    
    
    protected void updateMatches()
    {
        if (matches.isEmpty())
            return;
        
        matches.values().iterator().forEachRemaining(Match::update);
    }
    
    
    public void handleEvent(Event event, Player player)
    {
        switch (event.name) {
            case "findgame":
                if (player.getGameSession() != null || deque.contains(player))
                    return;
                
                deque.add(player);
                player.getClient().sendMessage("");
                break;
                
            case "quitgame":
                if (player.getGame() == null) {
                    deque.remove(player);
                    return;
                }
                
                break;
                
                
            default:
                return;
        }
    }


    public void close() { timer.cancel(); }


}
