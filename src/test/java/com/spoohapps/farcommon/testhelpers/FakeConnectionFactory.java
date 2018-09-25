package com.spoohapps.farcommon.testhelpers;

import com.spoohapps.farcommon.messaging.ConnectionFactory;
import com.spoohapps.farcommon.messaging.ConnectionSettings;
import com.spoohapps.farcommon.messaging.ConsumerConnection;
import com.spoohapps.farcommon.messaging.PublisherConnection;

import java.util.Objects;

public class FakeConnectionFactory implements ConnectionFactory {

    @Override
    public ConsumerConnection newConsumerConnection(ConnectionSettings settings) {
        return new FakeConsumerConnection();
    }

    @Override
    public void recycle() {

    }

    @Override
    public PublisherConnection newPublisherConnection(ConnectionSettings settings) {
        return new FakePublisherConnection();
    }

    @Override
    public int hashCode() {
        return Objects.hash(7);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        if (!FakeConnectionFactory.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        return true;
    }
}
