package com.spoohapps.farcommon.util;

import com.spoohapps.farcommon.model.Permission;
import com.spoohapps.farcommon.model.ReadDevicePermission;
import com.spoohapps.farcommon.model.WriteDevicePermission;

public class Permissions {

    public static Permission readDevice(String deviceId) {
        return new ReadDevicePermission(deviceId);
    }

    public static Permission writeDevice(String deviceId) {
        return new WriteDevicePermission(deviceId);
    }
}
