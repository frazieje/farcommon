package com.spoohapps.farcommon.model;

import com.spoohapps.farcommon.util.HexUtils;

public enum DeviceType {

    door_lock (0x4f);

    private final int intValue;

    DeviceType(int intValue) {

        this.intValue = intValue;

    }

    public int asInt() {

        return intValue;

    }

    public String asString() {

        return HexUtils.intToHex(intValue);

    }

}
