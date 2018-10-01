package com.spoohapps.farcommon.model;

import java.util.Objects;

public class ReadAnyDevicePermission implements Permission {

    private String profileId;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        Profile.verifyProfileId(profileId);
        this.profileId = profileId;
    }

    @Override
    public boolean checkFor(User user) {
        return user.getPermissions().contains(this);
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }

        if (!ReadAnyDevicePermission.class.isAssignableFrom(other.getClass())) {
            return false;
        }

        ReadAnyDevicePermission otherPermission = (ReadAnyDevicePermission) other;

        if (!profileId.equals(otherPermission.profileId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileId);
    }

}
