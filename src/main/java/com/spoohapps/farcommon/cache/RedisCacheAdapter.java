package com.spoohapps.farcommon.cache;

import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.KeyValue;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.api.async.RedisAsyncCommands;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RedisCacheAdapter implements CacheAdapter {

    private final RedisAsyncCommands<String, String> redisAsyncCommands;

    @Inject
    public RedisCacheAdapter(RedisAsyncCommands<String, String> asyncCommands) {
        redisAsyncCommands = asyncCommands;
    }

    @Override
    public CompletableFuture<String> get(String key) {
        return redisAsyncCommands.get(key)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Boolean> put(String key, String item) {
        return redisAsyncCommands.set(key, item)
                .thenApply(this::parseResponse)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Boolean> put(String key, String item, long expiresInSeconds) {
        return redisAsyncCommands.setex(key, expiresInSeconds, item)
                .thenApply(this::parseResponse)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Boolean> remove(String key) {
        return redisAsyncCommands.del(key)
                .thenApply(this::parseResponse)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Boolean> removeHash(String key) {
        return remove(key);
    }

    @Override
    public CompletableFuture<Map<String, String>> getStartingWith(String match) {
        return getKeys(new HashSet<>(), redisAsyncCommands.scan(ScanArgs.Builder.matches(match + "*")).toCompletableFuture(), match)
                .thenApply(keyValues -> {
                    Map<String, String> results = new ConcurrentHashMap<>();
                    for (KeyValue<String, String> kv : keyValues) {
                       results.put(kv.getKey(), kv.getValue());
                    }
                    return results;})
                .toCompletableFuture();
    }

    private CompletableFuture<List<KeyValue<String, String>>> getKeys(Set<String> keys, CompletableFuture<KeyScanCursor<String>> cursorFuture, String match) {
        return cursorFuture
                .thenCompose(c -> {
                    keys.addAll(c.getKeys());
                    if (c.isFinished()) {
                        return redisAsyncCommands.mget(keys.toArray(new String[] {}));
                    } else {
                        return getKeys(keys, redisAsyncCommands.scan(c, ScanArgs.Builder.matches(match + "*")).toCompletableFuture(), match);
                    }
                });
    }

    @Override
    public CompletableFuture<Map<String, String>> getHash(String key) {
        return redisAsyncCommands.hgetall(key)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<String> getHashItem(String hashKey, String itemKey) {
        return redisAsyncCommands.hget(hashKey, itemKey)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Boolean> putHashItem(String hashKey, String itemKey, String itemValue) {
        return redisAsyncCommands.hset(hashKey, itemKey, itemValue)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Boolean> removeHashItem(String hashKey, String itemKey) {
        return redisAsyncCommands.hdel(hashKey, itemKey)
                .thenApply(this::parseResponse)
                .toCompletableFuture();
    }

    private boolean parseResponse(String responseString) {
        if (responseString == null) {
            return false;
        }

        return responseString.equals("OK");

    }

    private boolean parseResponse(Long responseLong) {
        if (responseLong == null) {
            return false;
        }

        return responseLong == 1;
    }

}
