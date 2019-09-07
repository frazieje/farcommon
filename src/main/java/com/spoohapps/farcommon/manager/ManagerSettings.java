package com.spoohapps.farcommon.manager;

import java.util.concurrent.TimeUnit;

public interface ManagerSettings {
    long startDelay();
    TimeUnit startDelayTimeUnit();
    long timeout();
    TimeUnit timeoutTimeUnit();
    default float backoffScale() {
        return 1.0f;
    }
}
