package com.spoohapps.farcommon.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SimpleCache<T> implements Cache<T> {

    private final Logger logger = LoggerFactory.getLogger(SimpleCache.class);

    private final CacheAdapter cacheAdapter;
    private final ObjectMapper mapper;

    private String keyPrefix;

    private final Class<T> clazz;

    public SimpleCache(Class<T> clazz, String keyPrefix, CacheAdapter cacheAdapter, ObjectMapper objectMapper) {
        this.clazz = clazz;
        this.cacheAdapter = cacheAdapter;
        this.mapper = objectMapper;
        this.keyPrefix = keyPrefix != null ? keyPrefix : "";
    }

    @Override
    public CompletableFuture<T> get(String key) {
        logger.debug("Cache GET key: {}, cache type: {}", key, clazz.getSimpleName());
        return cacheAdapter.get(keyPrefix + key)
                .thenApply(this::processGetResult)
                .exceptionally(t -> null);
    }

    @Override
    public CompletableFuture<Boolean> put(String key, T item) {
        logger.debug("Cache PUT key: {}, cache type: {}", key, clazz.getSimpleName());
        String json = serialize(item);
        if (json == null) {
            return CompletableFuture.completedFuture(false);
        } else {
            return cacheAdapter.put(keyPrefix + key, json);
        }
    }

    @Override
    public CompletableFuture<Boolean> put(String key, T item, long expiresInSeconds) {
        logger.debug("Cache PUT key: {}, expiresIn: {}, cache type: {}", key, expiresInSeconds, clazz.getSimpleName());
        String json = serialize(item);
        if (json == null) {
            return CompletableFuture.completedFuture(false);
        } else {
            return cacheAdapter.put(keyPrefix + key, json, expiresInSeconds);
        }
    }

    @Override
    public CompletableFuture<T> getListItem(String listKey, long index) {
        logger.debug("Cache GET LIST ITEM: {}, at index {}", listKey, index);
        return cacheAdapter.getListItem(keyPrefix + listKey, index)
                .thenApply(this::processGetResult)
                .exceptionally(t -> null);
    }

    @Override
    public CompletableFuture<Boolean> setListItem(String listKey, long index, T item) {
        logger.debug("Cache SET LIST ITEM: {}, at index {}", listKey, index);
        String json = serialize(item);
        if (json == null) {
            return CompletableFuture.completedFuture(false);
        } else {
            return cacheAdapter.setListItem(keyPrefix + listKey, index, json)
                    .exceptionally(t -> null);
        }
    }

    @Override
    public CompletableFuture<Long> listPush(String listKey, List<T> items) {
        logger.debug("Cache LIST PUSH {}, {} items", listKey, items.size());
        List<String> itemJsons = serialize(items);
        if (itemJsons == null) {
            return CompletableFuture.completedFuture(null);
        }
        return cacheAdapter.listPush(keyPrefix + listKey, itemJsons)
                .exceptionally(t -> null);
    }

    @Override
    public CompletableFuture<Long> listPush(String listKey, T item) {
        logger.debug("Cache LIST PUSH {}, one item", listKey);
        String itemJson = serialize(item);
        if (itemJson == null) {
            return CompletableFuture.completedFuture(null);
        }
        return cacheAdapter.listPush(keyPrefix + listKey, itemJson)
                .exceptionally(t -> null);
    }

    @Override
    public CompletableFuture<T> listPop(String listKey) {
        logger.debug("Cache LIST POP {}", listKey);
        return cacheAdapter.listPop(keyPrefix + listKey)
                .thenApply(this::processGetResult)
                .exceptionally(t -> null);
    }

    @Override
    public CompletableFuture<Long> getListSize(String listKey) {
        return cacheAdapter.getListSize(keyPrefix + listKey)
                .exceptionally(t -> null);
    }

    @Override
    public CompletableFuture<Boolean> trimList(String listKey, long start, long stopInclusive) {
        logger.debug("Cache LIST TRIM {}, keeping from index {} through {}", listKey, start, stopInclusive);
        return cacheAdapter.trimList(keyPrefix + listKey, start, stopInclusive)
                .exceptionally(t -> false);
    }

    @Override
    public CompletableFuture<List<T>> getListRange(String listKey, long start, long stopInclusive) {
        logger.debug("Cache LIST GET RANGE {}, from index {} through {}", listKey, start, stopInclusive);
        return cacheAdapter.getListRange(keyPrefix + listKey, start, stopInclusive)
                .thenApply(l -> l.stream().map(this::processGetResult).collect(Collectors.toList()))
                .exceptionally(t -> new ArrayList<>());
    }

    @Override
    public CompletableFuture<Long> removeListItem(String listKey, T item) {
        logger.debug("Cache LIST REMOVE ITEM {}", listKey);
        return cacheAdapter.removeListItem(keyPrefix + listKey, serialize(item))
                .exceptionally(t -> null);
    }

    @Override
    public CompletableFuture<Long> listPushTail(String listKey, List<T> items) {
        logger.debug("Cache LIST PUSH TAIL {}, pushing {} items", listKey, items.size());
        List<String> itemJsons = serialize(items);
        if (itemJsons == null) {
            return CompletableFuture.completedFuture(null);
        }
        return cacheAdapter.listPushTail(keyPrefix + listKey, itemJsons)
                .exceptionally(t -> null);
    }

    @Override
    public CompletableFuture<Long> listPushTail(String listKey, T item) {
        logger.debug("Cache LIST PUSH TAIL {}, pushing one item", listKey);
        String itemJson = serialize(item);
        if (itemJson == null) {
            return CompletableFuture.completedFuture(null);
        }
        return cacheAdapter.listPushTail(keyPrefix + listKey, itemJson)
                .exceptionally(t -> null);
    }

    @Override
    public CompletableFuture<T> listPopTail(String listKey) {
        logger.debug("Cache LIST POP TAIL {}", listKey);
        return cacheAdapter.listPopTail(keyPrefix + listKey)
                .thenApply(this::processGetResult)
                .exceptionally(t -> null);
    }

    @Override
    public CompletableFuture<Boolean> remove(String keyToRemove) {
        logger.debug("Cache REMOVE key: {}, cache type: {}", keyToRemove, clazz.getSimpleName());
        return cacheAdapter.remove(keyPrefix + keyToRemove);
    }

    @Override
    public CompletableFuture<Map<String, T>> getStartingWith(String match) {
        logger.debug("Cache STARTINGWITH match: {}, cache type: {}", match, clazz.getSimpleName());
        return cacheAdapter.getStartingWith(keyPrefix + match)
                .thenApply(this::processHashResult);
    }

    @Override
    public CompletableFuture<T> getHashItem(String hashKey, String itemKey) {
        logger.debug("Cache HASH ITEM GET hashKey: {}, itemKey: {}, cache type: {}", hashKey, itemKey, clazz.getSimpleName());
        return cacheAdapter.getHashItem(keyPrefix + hashKey, itemKey)
                .thenApply(this::processGetResult)
                .exceptionally(t -> null);
    }

    @Override
    public CompletableFuture<Boolean> exists(String key) {
        logger.debug("Cache KEY EXISTS? key:{}", key);
        return cacheAdapter.exists(key);
    }

    @Override
    public CompletableFuture<Boolean> hashItemExists(String hashKey, String itemKey) {
        logger.debug("Cache HASH ITEM EXISTS? hashKey: {}, itemKey: {}", hashKey, itemKey);
        return cacheAdapter.hashItemExists(hashKey, itemKey);
    }

    @Override
    public CompletableFuture<Map<String, T>> getHash(String hashKey) {
        logger.debug("Cache HASH GET hashKey: {}, cache type: {}", hashKey, clazz.getSimpleName());
        return cacheAdapter.getHash(keyPrefix + hashKey)
                .thenApply(this::processHashResult);
    }

    @Override
    public CompletableFuture<Long> getHashSize(String hashKey) {
        logger.debug("Cache HASH SIZE GET hashKey: {}", hashKey);
        return cacheAdapter.getHashSize(keyPrefix + hashKey);
    }

    @Override
    public CompletableFuture<Boolean> putHashItem(String hashKey, String itemKey, T itemValue) {
        logger.debug("Cache HASH ITEM PUT hashKey: {}, itemKey: {}, cache type: {}", hashKey, itemKey, clazz.getSimpleName());
        return cacheAdapter.putHashItem(keyPrefix + hashKey, itemKey, serialize(itemValue));
    }

    @Override
    public CompletableFuture<Boolean> putHashItems(String hashKey, Map<String, T> items) {
        logger.debug("Cache HASH ITEMS PUT hashKey: {}, item count: {}", hashKey, items.size());
        return cacheAdapter.putHashItems(keyPrefix + hashKey,
                items.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                            String value = serialize(e.getValue());
                            return value != null ? value : "";
                        })));
    }

    @Override
    public CompletableFuture<Boolean> removeHashItem(String hashKey, String itemKey) {
        logger.debug("Cache HASH ITEM REMOVE hashKey: {}, itemKey: {}, cache type: {}", hashKey, itemKey, clazz.getSimpleName());
        return cacheAdapter.removeHashItem(keyPrefix + hashKey, itemKey);
    }

    @Override
    public CompletableFuture<Boolean> removeHash(String hashKey) {
        logger.debug("Cache HASH REMOVE hashKey: {}, cache type: {}", hashKey, clazz.getSimpleName());
        return cacheAdapter.removeHash(keyPrefix + hashKey);
    }

    @SuppressWarnings("ConstantConditions")
    private Map<String, T> processHashResult(Map<String, String> hashResult) {
        return hashResult.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> processGetResult(entry.getValue())));
    }

    private String serialize(T object) {
        logger.debug("Serializing object, cache type: {}", clazz.getSimpleName());
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.debug("error during serialization", e);
            return null;
        }
    }

    private List<String> serialize(List<T> objects) {
        List<String> result = new ArrayList<>();
        try {
            for (T item : objects) {
                String json = serialize(item);
                if (json != null) {
                    result.add(json);
                } else {
                    throw new IllegalArgumentException();
                }
            }
            return result;
        } catch (Exception e) {
            logger.debug("Error during serialization", e);
            return new ArrayList<>();
        }
    }

    private T processGetResult(String item) {
        if (item == null || item.trim().length() == 0) {
            return null;
        }
        logger.debug("Deserializing object, cache type: {}", clazz.getSimpleName());
        try {
            return mapper.readValue(item, clazz);
        } catch (Exception e) {
            logger.debug("error during deserialization", e);
            return null;
        }
    }
}
