package com.spoohapps.farcommon.connection.noop;

import com.spoohapps.farcommon.connection.ConnectionFactory;
import com.spoohapps.farcommon.connection.ConnectionSettings;
import com.spoohapps.farcommon.connection.ConsumerConnection;
import com.spoohapps.farcommon.connection.PublisherConnection;

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
