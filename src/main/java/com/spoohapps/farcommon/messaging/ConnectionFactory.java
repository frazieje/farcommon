package com.spoohapps.farcommon.messaging;

public interface ConnectionFactory {
    PublisherConnection newPublisherConnection(ConnectionSettings settings);
    ConsumerConnection newConsumerConnection(ConnectionSettings settings);
    void recycle();
}
