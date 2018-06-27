package com.spoohapps.farcommon.connection;

public interface TopicConsumerConnectionSettingsFactory {
    ConnectionSettings getSettings(String topic);
}
