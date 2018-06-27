package com.spoohapps.farcommon.connection.amqp091;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface Amqp091ConnectionSupplier {
    Amqp091Connection newConnection() throws IOException, TimeoutException;
}
