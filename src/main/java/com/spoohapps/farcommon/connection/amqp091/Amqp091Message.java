package com.spoohapps.farcommon.connection.amqp091;

import java.util.Map;

public interface Amqp091Message {
    Map<String, Object> getHeaders();
    String getRoutingKey();
    byte[] getBody();
}
