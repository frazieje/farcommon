package com.spoohapps.farcommon.model;


import com.spoohapps.farcommon.util.EUI48AddressValidator;
import com.spoohapps.farcommon.util.EUI48AddressValidatorImpl;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class EUI48Address implements Serializable {

    private transient EUI48AddressValidator eui48AddressValidator = new EUI48AddressValidatorImpl();

    public EUI48Address() {}

    public EUI48Address(String address) {

        String addressStr = address.trim();
        String nameStr = null;

        if (address.contains(" ")) {
            int index = address.indexOf(" ");
            addressStr = address.substring(0, index);
            nameStr = address.substring(index + 1);
        }

        if (!eui48AddressValidator.validate(addressStr))
            throw new IllegalArgumentException("Not a valid EUI-48 address.");

        data = hexStringToByteArray(addressStr);
        name = nameStr;
    }

    public EUI48Address(byte[] data) {
        this.data = data;
    }

    public EUI48Address(byte[] data, String name) {
        this(data);
        this.name = name;
    }

    public EUI48Address(String address, String name) {
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

    public static EUI48Address random() {
        Random random = new Random();
        String[] chars = new String[] { "A", "B", "C", "D", "E", "F" };

        String address = "";

        for (int i = 1; i <= 12; i++) {
            int num = Math.abs(random.nextInt()) % 16;
            if (num > 9) {
                address += chars[num-10];
            } else {
                address += num;
            }
            if (i % 2 == 0 && i != 12) {
                address += ":";
            }
        }

        return new EUI48Address(address);
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
        EUI48Address otherAddress = (EUI48Address)other;
        if (other == null)
            return false;

        return Arrays.equals(data, otherAddress.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
