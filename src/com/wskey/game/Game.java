package com.wskey.game;

import com.wskey.game.entities.Player;

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
    protected Timer timer = new Timer();
    protected HashMap<String, Match> matches = new HashMap<>();
    protected ConcurrentLinkedDeque<Player> deque = new ConcurrentLinkedDeque<>();


    /**
     * @param gameManager GameManager
     */
    public Game(GameManager gameManager)
    {
        this.gameManager = gameManager;
        timer.schedule(this, 0, 1000/60);
    }


    public void run()
    {
        updateQueue();
        if (!matches.isEmpty())
            matches.values().iterator().forEachRemaining(Match::update);
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
                }
            }
        }
    }


    /**
     * @return GameManager
     */
    public GameManager getGameManager() { return gameManager; }


    /**
     * @return Timer
     */
    public Timer getTimer() { return timer; }


    public void close()
    {
        timer.cancel();
    }


}
