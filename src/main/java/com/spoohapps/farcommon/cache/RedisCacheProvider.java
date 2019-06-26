package com.spoohapps.farcommon.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spoohapps.farcommon.manager.Manager;
import io.lettuce.core.api.StatefulRedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class RedisCacheProvider implements CacheProvider {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheProvider.class);

    private final Manager<StatefulRedisConnection<String, String>> connectionManager;

    private final ObjectMapper mapper;

    private final String keyPrefix;

    @Inject
    public RedisCacheProvider(Manager<StatefulRedisConnection<String, String>> connectionManager, ObjectMapper mapper, String keyPrefix) {
        logger.debug("init");
        this.connectionManager = connectionManager;
        this.mapper = mapper;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public <T> Cache<T> acquire(Class<T> clazz) {

        StatefulRedisConnection<String, String> connection = connectionManager.getResource();

        logger.debug("acquire redis connection, current connection = {}, isOpen = {}", (connection != null ? "not null" : "null"), ((connection != null && connection.isOpen()) ? "open" :  "n/a"));

        try {

            if (connection != null && connection.isOpen()) {
                return new SimpleCache<>(clazz, keyPrefix, new RedisCacheAdapter(connection.async()), mapper);
            } else {
                throw new IllegalStateException("redis connection unavailable");
            }

        } catch (Exception e) {
            logger.error("error acquiring cache", e);
            return null;
        }

    }

}
