package com.spoohapps.farcommon.model;

import java.util.Objects;

public class ReadDevicePermission implements Permission {

    private String deviceId;

    public ReadDevicePermission() {

    }

    public ReadDevicePermission(String deviceId) {
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

        if (!(other instanceof ReadDevicePermission)) {
            return false;
        }

        ReadDevicePermission otherPermission = (ReadDevicePermission) other;

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
