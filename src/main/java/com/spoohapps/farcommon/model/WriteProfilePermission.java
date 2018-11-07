package com.spoohapps.farcommon.model;

import java.util.Objects;

public class WriteProfilePermission implements Permission {

    private String profileId;

    public WriteProfilePermission() {

    }

    public WriteProfilePermission(String profileId) {
        Profile.verifyProfileId(profileId);
        this.profileId = profileId;
    }

    public String getProfileId() {
        return profileId;
    }

    @Override
    public boolean checkFor(User user) {
        return user.getPermissions().contains(this);
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof WriteProfilePermission)) {
            return false;
        }

        WriteProfilePermission otherPermission = (WriteProfilePermission) other;

        if (!profileId.equals(otherPermission.profileId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getSimpleName(), profileId);
    }
}
