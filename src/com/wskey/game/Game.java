package com.wskey.game;

import com.wskey.game.entities.Player;

import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author RomnSD
 */
public abstract class Game
{

    protected GameManager gameManager;
    protected Timer timer;
    protected LinkedHashMap<String, Match> matches = new LinkedHashMap<>();
    protected ConcurrentLinkedDeque<Player> deque = new ConcurrentLinkedDeque<>();

    public Game(GameManager gameManager)
    {
        this.gameManager = gameManager;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run()
            {
                update();
            }
        }, 0, 50);
    }

    public void update()
    {
        if (deque.isEmpty())
        {
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
                    if (player.isClosed())
                        continue;

                    freeMatch.addPlayer(player);
                }
            }
        }
        if (!matches.isEmpty())
            matches.values().iterator().forEachRemaining(Match::update);
    }

    public void close()
    {
        timer.cancel();
    }

}
