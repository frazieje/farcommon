package com.spoohapps.farcommon.model;

import java.util.Objects;

public class WriteDevicePermission implements Permission {

    private String deviceId;

    public WriteDevicePermission() {

    }

    public WriteDevicePermission(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public boolean checkFor(User user) {
        return user.getPermissions().contains(this);
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof WriteDevicePermission)) {
            return false;
        }

        WriteDevicePermission otherPermission = (WriteDevicePermission) other;

        if (!deviceId.equals(otherPermission.deviceId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getSimpleName(), deviceId);
    }
}
