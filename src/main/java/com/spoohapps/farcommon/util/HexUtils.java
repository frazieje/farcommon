package com.spoohapps.farcommon.util;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;

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

    public static int hexToInt(String hex) {
        String value = hex;
        if (hex.length() < 8) {
            StringBuilder prepend = new StringBuilder();
            for (int i = 0; i < (8 - hex.length()); i++) {
                prepend.append("0");
            }
            value = prepend + value;
        }
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            String byteStr = new String(new char[] { value.charAt(i*2), value.charAt((i*2)+1) });
            bytes[i] = hexToByte(byteStr);
        }

        ByteBuffer buffer = ByteBuffer.allocate(4).put(bytes);

        return buffer.getInt(0);
    }

    private static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }
}
