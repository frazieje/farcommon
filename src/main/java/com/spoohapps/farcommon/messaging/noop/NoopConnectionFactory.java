package com.spoohapps.farcommon.messaging.noop;

import com.spoohapps.farcommon.messaging.ConnectionFactory;
import com.spoohapps.farcommon.messaging.ConnectionSettings;
import com.spoohapps.farcommon.messaging.ConsumerConnection;
import com.spoohapps.farcommon.messaging.PublisherConnection;

public class NoopConnectionFactory implements ConnectionFactory {

    @Override
    public PublisherConnection newPublisherConnection(ConnectionSettings settings) {
        return null;
    }

    @Override
    public ConsumerConnection newConsumerConnection(ConnectionSettings settings) {
        return null;
    }

    @Override
    public void recycle() {

    }

}
