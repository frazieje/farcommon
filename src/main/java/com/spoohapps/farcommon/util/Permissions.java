package com.spoohapps.farcommon.util;

import com.spoohapps.farcommon.model.Permission;
import com.spoohapps.farcommon.model.ReadProfilePermission;
import com.spoohapps.farcommon.model.WriteProfilePermission;

public class Permissions {

    public static Permission readProfile(String profileId) {
        return new ReadProfilePermission(profileId);
    }

    public static Permission writeProfile(String profileId) {
        return new WriteProfilePermission(profileId);
    }
}
