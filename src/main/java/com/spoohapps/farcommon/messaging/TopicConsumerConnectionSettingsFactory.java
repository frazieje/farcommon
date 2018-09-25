package com.spoohapps.farcommon.messaging;

public interface TopicConsumerConnectionSettingsFactory {
    ConnectionSettings getSettings(String topic);
}
