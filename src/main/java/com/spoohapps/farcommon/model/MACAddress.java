package com.spoohapps.farcommon.model;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

public class MACAddress implements Serializable {

    public MACAddress() {}

    public final Pattern pattern = Pattern.compile("^((([0-9A-Fa-f]{2}[:-]?){5}([0-9A-Fa-f]{2}))|(([0-9A-Fa-f]{2}[:-]?){7}([0-9A-Fa-f]{2})))$");

    public MACAddress(String address) {

        String addressStr = address.trim();
        String nameStr = null;

        if (address.contains(" ")) {
            int index = address.indexOf(" ");
            addressStr = address.substring(0, index);
            nameStr = address.substring(index + 1);
        }

        if (!pattern.matcher(addressStr).matches())
            throw new IllegalArgumentException("Not a valid EUI-48 or EUI-64 address.");

        data = hexStringToByteArray(addressStr);
        name = nameStr;
    }

    public MACAddress(byte[] data) {
        this.data = data;
    }

    public MACAddress(byte[] data, String name) {
        this(data);
        this.name = name;
    }

    public MACAddress(String address, String name) {
        this(address + " " + name);
    }

    private byte[] data;

    private String name;

    private static byte[] hexStringToByteArray(String hex) {
        String s = hex.replaceAll("[:-]", "");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String byteArrayToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3 - 1];
        for ( int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            if (j != bytes.length-1)
                hexChars[j * 3 + 2] = ':';
        }
        return new String(hexChars);
    }

    public static MACAddress randomEUI48() {
        return random(12);
    }

    public static MACAddress randomEUI64() {
        return random(16);
    }

    private static MACAddress random(int lengthInHexChars) {
        Random random = new Random();
        String[] chars = new String[] { "A", "B", "C", "D", "E", "F" };

        StringBuilder address = new StringBuilder();

        for (int i = 1; i <= lengthInHexChars; i++) {
            int num = Math.abs(random.nextInt()) % 16;
            if (num > 9) {
                address.append(chars[num - 10]);
            } else {
                address.append(num);
            }
            if (i % 2 == 0 && i != lengthInHexChars) {
                address.append(":");
            }
        }

        return new MACAddress(address.toString());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return byteArrayToHexString(data);
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        String displayName = name != null && !name.equals("") ? name : "";
        return byteArrayToHexString(data) + (!displayName.isEmpty() ? (" " + displayName) : "");
    }

    @Override
    public boolean equals(Object other) {
        MACAddress otherAddress = (MACAddress)other;
        if (other == null)
            return false;

        return Arrays.equals(data, otherAddress.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
