package com.spoohapps.farcommon.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface Cache<T> {

    CompletableFuture<T> get(String key);

    CompletableFuture<T> getHashItem(String hashKey, String itemKey);

    CompletableFuture<Map<String, T>> getHash(String hashKey);

    CompletableFuture<Long> getHashSize(String hashKey);

    CompletableFuture<Boolean> putHashItem(String hashKey, String itemKey, T itemValue);

    CompletableFuture<Boolean> putHashItems(String hashKey, Map<String, T> items);

    CompletableFuture<Boolean> removeHashItem(String hashKey, String itemKey);

    CompletableFuture<Boolean> removeHash(String hashKey);

    CompletableFuture<Boolean> put(String key, T item);

    CompletableFuture<Boolean> put(String key, T item, long expiresInSeconds);

    CompletableFuture<T> getListItem(String listKey, long index);

    CompletableFuture<Boolean> setListItem(String listKey, long index, T item);

    CompletableFuture<Long> listPush(String listKey, List<T> items);

    CompletableFuture<Long> listPush(String listKey, T item);

    CompletableFuture<T> listPop(String listKey);

    CompletableFuture<Long> getListSize(String listKey);

    CompletableFuture<Boolean> trimList(String listKey, long start, long stopInclusive);

    CompletableFuture<List<T>> getListRange(String listKey, long start, long stopInclusive);

    CompletableFuture<Long> removeListItem(String listKey, T item);

    CompletableFuture<Long> listPushTail(String listKey, List<T> item);

    CompletableFuture<Long> listPushTail(String listKey, T item);

    CompletableFuture<T> listPopTail(String listKey);

    CompletableFuture<Boolean> remove(String toRemove);

    CompletableFuture<Map<String, T>> getStartingWith(String match);

}