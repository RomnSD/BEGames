package com.wskey;

import com.wskey.game.GameManager;
import com.wskey.server.WebSocketServer;


/**
 * @author RomnSD
 */
public class Main
{

    public static void main(String[] args)
    {
        WebSocketServer server = new WebSocketServer("localhost", 8080);
        server.start();

        GameManager gameManager = new GameManager(server, "/games");

        try {
            System.out.println("> Press ENTER to stop the server.");
            System.in.read();
        } catch (Throwable exception) { exception.printStackTrace(); }


        server.close();
    }


}
