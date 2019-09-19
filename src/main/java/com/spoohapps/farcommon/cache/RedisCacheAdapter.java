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
    public CompletableFuture<Boolean> exists(String key) {
        return redisAsyncCommands.exists(key)
                .thenApply(this::parseResponse)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Boolean> hashItemExists(String hashKey, String itemKey) {
        return redisAsyncCommands.hexists(hashKey, itemKey)
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
    public CompletableFuture<String> getListItem(String listKey, long index) {
        return redisAsyncCommands.lindex(listKey, index)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Boolean> setListItem(String listKey, long index, String item) {
        return redisAsyncCommands.lset(listKey, index, item)
                .thenApply(this::parseResponse)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Long> listPush(String listKey, List<String> items) {
        return redisAsyncCommands.lpush(listKey, items.toArray(new String[0]))
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Long> listPush(String listKey, String item) {
        return redisAsyncCommands.lpush(listKey, item)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<String> listPop(String listKey) {
        return redisAsyncCommands.lpop(listKey)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Long> getListSize(String listKey) {
        return redisAsyncCommands.llen(listKey)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Boolean> trimList(String listKey, long start, long stopInclusive) {
        return redisAsyncCommands.ltrim(listKey, start, stopInclusive)
                .thenApply(this::parseResponse)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<List<String>> getListRange(String listKey, long start, long stopInclusive) {
        return redisAsyncCommands.lrange(listKey, start, stopInclusive)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Long> removeListItem(String listKey, String itemValue) {
        return redisAsyncCommands.lrem(listKey, 0, itemValue)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Long> listPushTail(String listKey, List<String> items) {
        return redisAsyncCommands.rpush(listKey, items.toArray(new String[0]))
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<Long> listPushTail(String listKey, String item) {
        return redisAsyncCommands.rpush(listKey, item)
                .toCompletableFuture();
    }

    @Override
    public CompletableFuture<String> listPopTail(String listKey) {
        return redisAsyncCommands.rpop(listKey)
                .toCompletableFuture();
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
    public CompletableFuture<Long> getHashSize(String hashKey) {
        return redisAsyncCommands.hlen(hashKey)
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
    public CompletableFuture<Boolean> putHashItems(String hashKey, Map<String, String> items) {
        return redisAsyncCommands.hmset(hashKey, items)
                .thenApply(this::parseResponse)
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
