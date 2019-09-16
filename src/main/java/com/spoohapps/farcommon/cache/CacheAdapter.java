package com.spoohapps.farcommon.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface CacheAdapter {

    CompletableFuture<String> get(String key);

    CompletableFuture<Map<String, String>> getHash(String key);

    CompletableFuture<Long> getHashSize(String hashKey);

    CompletableFuture<String> getHashItem(String hashKey, String itemKey);

    CompletableFuture<Boolean> putHashItem(String hashKey, String itemKey, String itemValue);

    CompletableFuture<Boolean> putHashItems(String hashKey, Map<String, String> items);

    CompletableFuture<Boolean> removeHashItem(String hashKey, String itemKey);

    CompletableFuture<Boolean> put(String key, String item);

    CompletableFuture<Boolean> put(String key, String item, long expiresInSeconds);

    CompletableFuture<Boolean> remove(String key);

    CompletableFuture<Boolean> removeHash(String key);

    CompletableFuture<String> getListItem(String listKey, long index);

    CompletableFuture<Boolean> setListItem(String listKey, long index, String item);

    CompletableFuture<Long> listPush(String listKey, List<String> items);

    CompletableFuture<Long> listPush(String listKey, String item);

    CompletableFuture<String> listPop(String listKey);

    CompletableFuture<Long> getListSize(String listKey);

    CompletableFuture<Boolean> trimList(String listKey, long start, long stopInclusive);

    CompletableFuture<List<String>> getListRange(String listKey, long start, long stopInclusive);

    CompletableFuture<Long> removeListItem(String listKey, String item);

    CompletableFuture<Long> listPushTail(String listKey, List<String> items);

    CompletableFuture<Long> listPushTail(String listKey, String item);

    CompletableFuture<String> listPopTail(String listKey);

    CompletableFuture<Map<String, String>> getStartingWith(String key);

}
