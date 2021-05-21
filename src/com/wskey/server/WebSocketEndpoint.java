package com.wskey.server;

import com.wskey.protocol.Frame;


/**
 * @author RomnSD
 */
public abstract class WebSocketEndpoint
{

    protected String route;
    protected WebSocketServer server;


    /**
     * @param route String
     */
    public WebSocketEndpoint(String route) { this.route = route; }


    /**
     * @return String
     */
    public String getRoute() {
        return route;
    }


    /**
     * @return WebSocketServer
     */
    public WebSocketServer getServer() { return server; }


    /**
     * @param server WebSocketServer
     */
    public void setServer(WebSocketServer server) { this.server = server; }


    /**
     * @param client WebSocketClient
     * @return       boolean
     */
    public abstract boolean onLogin(WebSocketClient client);


    /**
     * @param frame  Frame
     * @param client WebSocketClient
     */
    public abstract void onData(Frame frame, WebSocketClient client);


    /**
     * @param client WebSocketClient
     */
    public abstract void onQuit(WebSocketClient client);


}
