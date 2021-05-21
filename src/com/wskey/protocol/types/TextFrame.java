package com.wskey.protocol.types;

import com.wskey.protocol.Frame;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


/**
 * @author RomnSD
 */
public class TextFrame extends Frame
{

    public String data = "";


    public TextFrame() {}


    /**
     * @param data String
     */
    public TextFrame(String data) { this.data = data; }


    @Override
    public ByteBuffer encode()
    {
        if (data == null)
            return null;

        payload = data.getBytes(StandardCharsets.UTF_8);
        return super.encode();
    }


    @Override
    public void decode()
    {
        if (payload == null)
            return;

        data    = new String(payload);
        payload = null;
    }


    @Override
    public byte getOpCode() { return TEXT_FRAME; }


}
