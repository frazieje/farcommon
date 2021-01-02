package com.spoohapps.farcommon.model;

import java.util.Objects;
import java.util.regex.Pattern;

public class DeviceId {

    public static Pattern pattern = Pattern.compile("^[0-9A-Fa-f]{13,20}$");

    private final DeviceType type;
    private final MACAddress address;

    public DeviceId(String deviceId) {
        if (deviceId == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
        if (!pattern.matcher(deviceId).matches()) {
            throw new IllegalArgumentException("Argument not a device id");
        }
        int len = deviceId.length();
        int total = len <= 16 ? 12 : 16;
        this.type = DeviceType.fromString(deviceId.substring(0, len - total));
        this.address = new MACAddress(deviceId.substring(len - total));
    }

    public DeviceId(DeviceType type, MACAddress address) {
        if (type == null || address == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        this.type = type;
        this.address = address;
    }

    public static boolean isValid(String deviceId) {

        try {
            new DeviceId(deviceId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public DeviceType getType() {
        return type;
    }

    public MACAddress getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return type.asString() +
                address.getAddress()
                        .replaceAll("[:-]", "")
                        .toLowerCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceId deviceId = (DeviceId) o;
        return type == deviceId.type &&
                Objects.equals(address, deviceId.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, address);
    }
}