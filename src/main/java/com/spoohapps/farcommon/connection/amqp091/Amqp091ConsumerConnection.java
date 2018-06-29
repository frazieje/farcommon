package com.spoohapps.farcommon.connection.amqp091;

import com.spoohapps.farcommon.model.Message;
import com.spoohapps.farcommon.connection.ConsumerConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.function.Consumer;

public class Amqp091ConsumerConnection implements ConsumerConnection {

    private String exchange;
    private String queue;
    private String routingKey;
    private Runnable onClosed;

    private Amqp091ChannelSupplier channelSupplier;
    private Amqp091Channel channel;

    private Consumer<Message> messageConsumer;

    private final Logger logger = LoggerFactory.getLogger(Amqp091ConsumerConnection.class);

    public Amqp091ConsumerConnection(Amqp091ChannelSupplier channelSupplier, String exchange, String queue, String routingKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
        this.channelSupplier = channelSupplier;
    }

    @Override
    public void onConsume(Consumer<Message> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    @Override
    public String getDescription() {
        try {
            return "Consuming from " + channel.getConnectionName();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void open() {
        logger.info("opening consumer connection...");
        try {
            channel = channelSupplier.getChannel();
            channel.addShutdownListener(this::handleShutdown);
            channel.exchangeDeclare(exchange);
            channel.queueDeclare(queue);
            channel.queueBind(queue, exchange, routingKey);
            channel.consume(queue, this::consumeInternal, this::handleShutdown);
            logger.info("consumer connection opened.");
        } catch (Exception e) {
            logger.error("Error opening consumer connection", e);
            closeInternal();
        }
    }

    private void consumeInternal(Amqp091Message message) {
        Message newMessage =
                new Message(
                        message.getRoutingKey(),
                        message.getBody(),
                        new HashMap<>(message.getHeaders()));

        if (messageConsumer != null)
            messageConsumer.accept(newMessage);
    }

    @Override
    public void onClosed(Runnable closed) {
        this.onClosed = closed;
    }

    private void handleShutdown(String message) {
        logger.info("consumer connection shutdown: " + message);
        notifyShutdown();
    }

    @Override
    public void close() {
        logger.info("explicitly closing consumer connection");
        closeInternal();
    }

    private void closeInternal() {
        logger.info("internally closing consumer connection");
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
