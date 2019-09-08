package com.spoohapps.farcommon.manager;

import java.util.concurrent.TimeUnit;

public interface ManagerSettings {
    long startDelay();
    TimeUnit startDelayTimeUnit();
    long timeout();
    TimeUnit timeoutTimeUnit();
    default boolean backoffEnabled() {
        return false;
    }
    default int[] backoffMultipliers() {
        return new int[] { 1, 2, 4, 8, 16, 32 };
    }
    default float backoffScale() {
        return 1.0f;
    }
}
