package com.wskey.protocol.types;

import com.wskey.protocol.Frame;

import java.nio.ByteBuffer;


/**
 * @author RomnSD
 */
public class BinaryFrame extends Frame
{

    public byte[] data;


    public BinaryFrame() {}


    /**
     * @param data byte[]
     */
    public BinaryFrame(byte[] data) { this.data = data; }


    @Override
    public ByteBuffer encode()
    {
        if (data == null)
            return null;

        payload = data;

        return super.encode();
    }


    @Override
    public void decode()
    {
        if (payload == null)
            return;

        data    = payload;
        payload = null;
    }


    @Override
    public byte getOpCode() { return BINARY_FRAME; }


}
