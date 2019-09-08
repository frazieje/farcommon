package com.spoohapps.farcommon.model;

import com.spoohapps.farcommon.util.HexUtils;

public enum DeviceType {

    door_lock (0x4f, "Door Lock"),

    window_shade (0x6b, "Window Shade");

    private final int intValue;

    private final String description;

    DeviceType(int intValue, String description) {

        this.intValue = intValue;
        this.description = description;

    }

    public int asInt() {

        return intValue;

    }

    public String asString() {

        return HexUtils.intToHex(intValue);

    }

    public static DeviceType fromString(String deviceTypeHex) {
        for (DeviceType deviceType : values()) {
            if (deviceType.asInt() == HexUtils.hexToInt(deviceTypeHex)) {
                return deviceType;
            }
        }
        throw new IllegalArgumentException("No device type associated with value '" + deviceTypeHex + "'");
    }

    @Override
    public String toString() {
        return description;
    }

}
