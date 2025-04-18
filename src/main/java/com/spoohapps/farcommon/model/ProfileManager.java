package com.spoohapps.farcommon.model;

import java.util.function.Consumer;

public interface ProfileManager {
    void set(Profile profile);
    Profile get();
    void start();
    void stop();
    void onChanged(Consumer<Profile> profileFunction);
}
