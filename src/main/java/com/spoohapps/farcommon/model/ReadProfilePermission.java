package com.spoohapps.farcommon.model;

import java.util.Objects;

public class ReadProfilePermission implements Permission {

    private String profileId;

    public ReadProfilePermission() {

    }

    public ReadProfilePermission(String profileId) {
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

        if (!(other instanceof ReadProfilePermission)) {
            return false;
        }

        ReadProfilePermission otherPermission = (ReadProfilePermission) other;

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
