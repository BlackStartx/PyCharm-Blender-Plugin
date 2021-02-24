package util.core;

import java.nio.ByteBuffer;

public class MyByteConversion {

    public static byte[] intToByteArray(int a) {
        byte[] ret = new byte[4];
        ret[3] = (byte) (a & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    public static String byteToString(byte[] message) {
        return new String(message);
    }

    public static int byteToInt(byte[] message) {
        ByteBuffer wrapped = ByteBuffer.wrap(message);
        return wrapped.getInt();
    }
}
