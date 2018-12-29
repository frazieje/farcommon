package com.spoohapps.farcommon.model;

import java.util.Objects;

public class ManagerPermission implements Permission {

    public ManagerPermission() {}

    @Override
    public boolean checkFor(User user) {
        return user.getPermissions().contains(this);
    }

    @Override
    public boolean equals(Object other) {

        if (!(other instanceof ManagerPermission)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getSimpleName());
    }
}
