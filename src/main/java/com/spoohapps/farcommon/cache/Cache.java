package com.spoohapps.farcommon.cache;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface Cache<T> {

    CompletableFuture<T> get(String key);

    CompletableFuture<T> getHashItem(String hashKey, String itemKey);

    CompletableFuture<Map<String, T>> getHash(String hashKey);

    CompletableFuture<Boolean> putHashItem(String hashKey, String itemKey, T itemValue);

    CompletableFuture<Boolean> removeHashItem(String hashKey, String itemKey);

    CompletableFuture<Boolean> removeHash(String hashKey);

    CompletableFuture<Boolean> put(String key, T item);

    CompletableFuture<Boolean> put(String key, T item, long expiresInSeconds);

    CompletableFuture<Boolean> remove(String toRemove);

    CompletableFuture<Map<String, T>> getStartingWith(String match);

}