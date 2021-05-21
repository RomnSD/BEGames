package com.wskey.protocol;

import com.wskey.protocol.types.*;

import java.nio.ByteBuffer;


/**
 * @author RomnSD
 */
public abstract class Frame
{

    public static final byte CONTINUATION_FRAME = 0x00;
    public static final byte TEXT_FRAME         = 0x01;
    public static final byte BINARY_FRAME       = 0x02;
    public static final byte CLOSE_FRAME        = 0x08;
    public static final byte PING_FRAME         = 0x09;
    public static final byte PONG_FRAME         = 0xA;

    public boolean fin;
    public byte[]  payload = new byte[0];


    public ByteBuffer encode()
    {
        if (payload == null)
            return null;

        byte length = (byte) payload.length;
        int  bytes  = 2;

        if (payload.length > 125) {
            if (payload.length > Short.MAX_VALUE) {
                length = 127;
                bytes  = 8;
            } else {
                length = 126;
                bytes  = 2;
            }
        }

        ByteBuffer buffer = ByteBuffer.allocate(bytes + payload.length);
        buffer.put((byte) (getOpCode() | 0x80));
        buffer.put(length);

        if (length == 126)
            buffer.putShort((short) payload.length);

        if (length == 127)
            buffer.putLong(payload.length);

        buffer.put(payload);
        buffer.position(0);

        return buffer;
    }


    public void decode() {}


    /**
     * @return byte
     */
    protected abstract byte getOpCode();


    /**
     * @param buffer ByteBuffer
     * @return       Frame
     */
    public static Frame decodeRawFrame(ByteBuffer buffer)
    {
        byte one = buffer.get();
        byte two = buffer.get();

        boolean fin = (one & 0x80) == 0;

        if ((two & 0x80) == 0)
            return null;

        byte opCode = (byte) (one & 0xF);
        int  length = (two & 0x7F);

        Frame frame;

        switch (opCode) {

            case CONTINUATION_FRAME:
                return null; // TODO: Agregar continuacion

            case TEXT_FRAME:
                frame = new TextFrame();
                break;

            case BINARY_FRAME:
                frame = new BinaryFrame();
                break;

            case CLOSE_FRAME:
                frame = new CloseFrame();
                break;

            case PING_FRAME:
                return new PingFrame();

            case PONG_FRAME:
                return new PongFrame();

            default:
                return null;
        }

        if (length == 0x7E) length = buffer.getShort(); else if (length == 0x7F) length = (int) buffer.getLong();

        if (length < 0)
            return null;

        byte[] mask = new byte[] {
                buffer.get(), buffer.get(),
                buffer.get(), buffer.get()
        };

        byte[] payload = new byte[length];
        buffer.get(payload,0, length);

        for (int i = 0; i < length; i++)
            payload[i] ^= mask[i % 4];

        frame.fin     = fin;
        frame.payload = payload;

        return frame;
    }


}
