package com.wskey.protocol.types;

import com.wskey.protocol.Frame;


/**
 * @author RomnSD
 */
public class PongFrame extends Frame
{

    public PongFrame() { }

    @Override
    public byte getOpCode() { return PONG_FRAME; }

}
