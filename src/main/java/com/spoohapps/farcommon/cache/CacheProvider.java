package com.spoohapps.farcommon.cache;

public interface CacheProvider {
    <T> Cache<T> acquire(Class<T> clazz);
}
