package com.spoohapps.farcommon.connection;

import com.spoohapps.farcommon.model.Message;

import java.util.function.Consumer;

public interface ConsumerConnection {
    void open();
    void close();
    void onClosed(Runnable closed);
    void onConsume(Consumer<Message> messageConsumer);
    String getDescription();
}
