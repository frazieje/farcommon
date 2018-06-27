package com.spoohapps.farcommon.connection;

public interface ConnectionFactory {
    PublisherConnection newPublisherConnection(ConnectionSettings settings);
    ConsumerConnection newConsumerConnection(ConnectionSettings settings);
}
