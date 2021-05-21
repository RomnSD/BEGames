package com.wskey.protocol.types;

import com.wskey.protocol.Frame;


/**
 * @author RomnSD
 */
public class PingFrame extends Frame
{


    public PingFrame() { }


    @Override
    public byte getOpCode() { return PING_FRAME; }


}
