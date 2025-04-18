package com.spoohapps.farcommon.messaging.amqp091;

import com.spoohapps.farcommon.messaging.ConnectionFactory;
import com.spoohapps.farcommon.messaging.ConnectionSettings;
import com.spoohapps.farcommon.messaging.ConsumerConnection;
import com.spoohapps.farcommon.messaging.PublisherConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class Amqp091ConnectionFactory implements ConnectionFactory  {

    private final Amqp091ConnectionSupplier connectionSupplier;

    private final Logger logger = LoggerFactory.getLogger(Amqp091ConnectionFactory.class);

    private Amqp091Connection connection;

    private List<Amqp091Channel> channels;

    private boolean autoDisconnect = true;

    public Amqp091ConnectionFactory(Amqp091ConnectionSupplier connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
        channels = new ArrayList<>();
    }

    public Amqp091ConnectionFactory(Amqp091ConnectionSupplier connectionSupplier, boolean autoDisconnect) {
        this(connectionSupplier);
        this.autoDisconnect = autoDisconnect;
    }

    @Override
    public ConsumerConnection newConsumerConnection(ConnectionSettings settings) {
        return new Amqp091ConsumerConnection(
                this::getChannel,
                settings.get(Amqp091ConsumerConnectionSettings.SettingsKeys.EXCHANGE),
                settings.get(Amqp091ConsumerConnectionSettings.SettingsKeys.QUEUE),
                settings.get(Amqp091ConsumerConnectionSettings.SettingsKeys.ROUTING_KEY));
    }

    @Override
    public PublisherConnection newPublisherConnection(ConnectionSettings settings) {
        return new Amqp091PublisherConnection(
                this::getChannel,
                settings.get(Amqp091PublisherConnectionSettings.SettingsKeys.EXCHANGE));
    }

    private synchronized Amqp091Channel getChannel() throws IOException, TimeoutException {
        if (connection == null) {
            connection = connectionSupplier.newConnection();
            connection.addShutdownListener(this::connectionShutdown);
        }
        Amqp091Channel channel = connection.createChannel();
        channel.addShutdownListener(reason -> channelShutdown(connection, channel, reason));
        channels.add(channel);
        return channel;
    }

    private synchronized void connectionShutdown(String reason) {
        logger.debug("Shutdown signal from connection: {}", connection.getName());
        connection = null;
        channels.clear();
    }

    private synchronized void channelShutdown(Amqp091Connection owner, Amqp091Channel channel, String reason) {
        if (channels.remove(channel));
        {
            logger.debug("Shutdown signal from channel on connection: {}. remaining channels = {}", connection.getName(), channels.size());
            if (autoDisconnect && owner == connection && connection != null && channels.size() == 0) {
                logger.debug("All channels gone for connection: {}, closing connection.", connection.getName());
                try {
                    connection.close();
                } catch (Exception e) {
                    logger.error("Error closing connection", e);
                    connection = null;
                }
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionSupplier);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!Amqp091ConnectionFactory.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final Amqp091ConnectionFactory other = (Amqp091ConnectionFactory) obj;

        if (autoDisconnect != other.autoDisconnect)
            return false;

        if (connectionSupplier == null ? other.connectionSupplier != null : !connectionSupplier.equals(other.connectionSupplier))
            return false;

        return true;
    }

    @Override
    public synchronized void recycle() {
        logger.debug("recycle connection: {}", connection != null ? connection.getName() : "null");
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                logger.error("Error closing connection", e);
                connection = null;
            }
        }
    }

}
