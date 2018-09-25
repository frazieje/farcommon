package com.spoohapps.farcommon.messaging.amqp091;

import java.io.IOException;
import java.util.function.Consumer;

public interface Amqp091Connection {
    Amqp091Channel createChannel() throws IOException;
    void close() throws IOException;
    String getName();
    void addShutdownListener(Consumer<String> message);
}
