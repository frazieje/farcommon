package com.spoohapps.farcommon.messaging.amqp091;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface Amqp091ChannelSupplier {
    Amqp091Channel getChannel() throws IOException, TimeoutException;
}
