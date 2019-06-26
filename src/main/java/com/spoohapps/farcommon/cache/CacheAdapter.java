package com.spoohapps.farcommon.cache;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CacheAdapter {

    CompletableFuture<String> get(String key);

    CompletableFuture<Map<String, String>> getHash(String key);

    CompletableFuture<String> getHashItem(String hashKey, String itemKey);

    CompletableFuture<Boolean> putHashItem(String hashKey, String itemKey, String itemValue);

    CompletableFuture<Boolean> removeHashItem(String hashKey, String itemKey);

    CompletableFuture<Boolean> put(String key, String item);

    CompletableFuture<Boolean> put(String key, String item, long expiresInSeconds);

    CompletableFuture<Boolean> remove(String key);

    CompletableFuture<Boolean> removeHash(String key);

    CompletableFuture<Map<String, String>> getStartingWith(String key);

}
