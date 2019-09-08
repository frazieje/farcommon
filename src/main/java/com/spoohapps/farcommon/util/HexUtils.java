package com.spoohapps.farcommon.util;

import java.nio.ByteBuffer;

public class HexUtils {

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String intToHex(int value) {
        return intToHex(value, false);
    }

    public static String intToHex(int value, boolean leadingZeros) {
        String s = bytesToHex(ByteBuffer.allocate(4).putInt(value).array());
        if (leadingZeros) {
            return s;
        }
        return s.replaceAll("^0+", "");
    }

}
