package com.spoohapps.farcommon.messaging.amqp091;

import com.spoohapps.farcommon.messaging.ConnectionSettings;
import com.spoohapps.farcommon.messaging.TopicConsumerConnectionSettingsFactory;

public class Amqp091TopicConsumerConnectionSettingsFactory implements TopicConsumerConnectionSettingsFactory {

    private final Amqp091ConsumerConnectionSettings baseSettings;

    public Amqp091TopicConsumerConnectionSettingsFactory(Amqp091ConsumerConnectionSettings baseSettings) {
        this.baseSettings = baseSettings;
    }

    @Override
    public ConnectionSettings getSettings(String topic) {
        baseSettings.setRoutingKey(topic);
        return baseSettings;
    }
}
