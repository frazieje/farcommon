package com.spoohapps.farcommon.testhelpers;

import com.spoohapps.farcommon.model.Message;
import com.spoohapps.farcommon.connection.ConsumerConnection;

import java.util.function.Consumer;

public class FakeConsumerConnection implements ConsumerConnection {
    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public void onClosed(Runnable closed) {

    }

    @Override
    public void onConsume(Consumer<Message> messageConsumer) {

    }

    @Override
    public String getDescription() {
        return null;
    }
}
