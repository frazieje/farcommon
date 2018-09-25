package com.spoohapps.farcommon.model;

public interface Permission {

    String getName();

    boolean checkFor(User user);

}
