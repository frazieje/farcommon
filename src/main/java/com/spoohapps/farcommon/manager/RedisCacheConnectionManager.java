package com.spoohapps.farcommon.manager;

import com.spoohapps.farcommon.cache.CacheConnectionSettings;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.ScheduledExecutorService;

public class RedisCacheConnectionManager extends AbstractManager<StatefulRedisConnection<String, String>> {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheConnectionManager.class);

    private final CacheConnectionSettings settings;

    private volatile StatefulRedisConnection<String, String> statefulRedisConnection;

    @Inject
    public RedisCacheConnectionManager(
            ScheduledExecutorService scheduledExecutorService,
            CacheConnectionSettings settings,
            @Named("RedisCacheConnectionManagerSettings") ManagerSettings managerSettings) {

        super(scheduledExecutorService, managerSettings);
        this.settings = settings;

    }

    @Override
    protected boolean doStart() {
        logger.info("Starting redis cache connection manager... {}", this);
        return true;
    }


    @Override
    protected void doProcess() {
        if (statefulRedisConnection == null || !statefulRedisConnection.isOpen()) {
            logger.debug("no active redis connection, creating");
            statefulRedisConnection = tryCreateConnection();
        }
    }

    @Override
    protected StatefulRedisConnection<String, String> doGetResource() {

        return (statefulRedisConnection != null && statefulRedisConnection.isOpen()) ? statefulRedisConnection : null;

    }

    private StatefulRedisConnection<String, String> tryCreateConnection() {
        String host = settings.host();
        int port = settings.port();
        if (host != null && port > 0) {
            try {
                return RedisClient.create()
                        .connect(RedisURI.create(host, port));
            } catch (Exception e) {
                logger.error("error connecting to redis", e);
                return null;
            }
        }
        return null;
    }

    @Override
    protected void doStop() {
        logger.debug("stopping redis cache connection manager...");
        if (statefulRedisConnection != null) {
            statefulRedisConnection.close();
        }
    }

}
