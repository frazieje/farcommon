package com.spoohapps.farcommon.manager;

public interface Manager<T> {
    void start();
    void stop();
    T getResource();

}
