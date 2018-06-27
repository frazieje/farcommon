package com.spoohapps.farcommon.testhelpers;

import com.spoohapps.farcommon.model.Message;
import com.spoohapps.farcommon.connection.PublisherConnection;

public class FakePublisherConnection implements PublisherConnection {
    @Override
    public void publish(Message message) {

    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void open() {

    }

    @Override
    public void close() {

    }

    @Override
    public void onClosed(Runnable closed) {

    }
}
