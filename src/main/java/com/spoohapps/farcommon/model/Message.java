package com.spoohapps.farcommon.model;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private final String topic;

    private final byte[] payload;

    private final Map<String, Object> headers;

    public String getTopic() {
        return topic;
    }

    public byte[] getPayload() {
        return payload;
    }

    public Object getHeader(String key) {
        return headers.get(key);
    }

    public void addHeader(String key, Object value) {
        headers.put(key, value);
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public Message(String topic, byte[] payload) {
        this.topic = topic;
        this.payload = payload;
        this.headers = new HashMap<>();
    }

    public Message(String topic, byte[] payload, Map<String, Object> headers) {
        this.topic = topic;
        this.payload = payload;
        this.headers = headers;
    }

}
