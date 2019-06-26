package com.spoohapps.farcommon.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public CompletableFuture<Map<String, T>> getHash(String hashKey) {
        logger.debug("Cache HASH GET hashKey: {}, cache type: {}", hashKey, clazz.getSimpleName());
        return cacheAdapter.getHash(keyPrefix + hashKey)
                .thenApply(this::processHashResult);
    }

    @Override
    public CompletableFuture<Boolean> putHashItem(String hashKey, String itemKey, T itemValue) {
        logger.debug("Cache HASH ITEM PUT hashKey: {}, itemKey: {}, cache type: {}", hashKey, itemKey, clazz.getSimpleName());
        return cacheAdapter.putHashItem(keyPrefix + hashKey, itemKey, serialize(itemValue));
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
