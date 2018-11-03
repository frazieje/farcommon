package com.spoohapps.farcommon.messaging.amqp091;

import com.spoohapps.farcommon.model.Message;
import com.spoohapps.farcommon.messaging.PublisherConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Amqp091PublisherConnection implements PublisherConnection {

    private String exchange;
    private Amqp091ChannelSupplier channelSupplier;
    private Amqp091Channel channel;

    private Runnable onClosed;

    private final Logger logger = LoggerFactory.getLogger(Amqp091PublisherConnection.class);

    public Amqp091PublisherConnection(Amqp091ChannelSupplier channelSupplier, String exchange) {
        this.exchange = exchange;
        this.channelSupplier = channelSupplier;
    }

    @Override
    public void publish(Message message) {
        logger.info("Publishing message to {} exchange, routed for '{}', payload size {} bytes", exchange, message.getTopic(), message.getPayload().length);

        try {
            channel.publish(exchange,
                    message.getTopic(),
                    message.getHeaders(),
                    message.getPayload());
        } catch (Exception e) {
            logger.error("Error publishing message", e);
            closeInternal();
        }
    }

    @Override
    public String getDescription() {
        try {
            return "Publishing to " + channel.getConnectionName();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void open() {
        logger.info("opening publisher connection...");
        try {
            channel = channelSupplier.getChannel();
            channel.addShutdownListener(this::handleShutdown);
            logger.info("publisher connection opened");
        } catch (Exception e) {
            logger.error("Error opening publisher channel", e);
            closeInternal();
        }
    }

    @Override
    public void onClosed(Runnable closed) {
        this.onClosed = closed;
    }

    private void handleShutdown(String message) {
        logger.info("publisher connection shutdown: " + message);
        notifyShutdown();
    }

    @Override
    public void close() {
        logger.info("explicitly closing publisher connection");
        closeInternal();
    }

    private void closeInternal() {
        logger.info("internally closing publisher connection");
        try {
            if (channel != null) {
                logger.info("channel not null, closing");
                channel.close();
            } else {
                logger.info("channel null, notifying");
                notifyShutdown();
            }
        } catch (Exception e) {
            logger.error("Error closing connection", e);
            notifyShutdown();
        }
    }

    private void notifyShutdown() {
        if (onClosed != null) {
            onClosed.run();
        }
    }
}
