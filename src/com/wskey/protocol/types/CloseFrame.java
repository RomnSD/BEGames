package com.wskey.protocol.types;

import com.wskey.protocol.Frame;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


/**
 * @author RomnSD
 */
public class CloseFrame extends Frame
{

    public short code;
    public String reason;
    
    public static short SHUTDOWN = 1001;
    public static short WRONG_FRAME = 1003;
    public static short UNEXPECTED_BEHAVIOR = 1011;


    public CloseFrame() { }


    /**
     * @param reason String
     * @param code   Short
     */
    public CloseFrame(Short code, String reason)
    {
        this.code   = code;
        this.reason = reason;
    }


    @Override
    public ByteBuffer encode()
    {
        if (reason == null)
            return null;

        ByteBuffer buffer = ByteBuffer.allocate(2 + reason.length());
                   buffer.putShort(code);
                   buffer.put(reason.getBytes(StandardCharsets.UTF_8));

        payload = buffer.array();

        return super.encode();
    }


    @Override
    public void decode()
    {
        if (payload == null || payload.length < 3)
            return;

        ByteBuffer buffer = ByteBuffer.wrap(payload);

        code = buffer.getShort();
        reason = new String(buffer.array()).substring(2);
    }


    @Override
    public byte getOpCode() { return CLOSE_FRAME; }


}
