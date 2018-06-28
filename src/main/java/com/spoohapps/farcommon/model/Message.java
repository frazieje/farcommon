package com.spoohapps.farcommon.model;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private String topic;

    private byte[] payload;

    private Map<String, Object> headers = new HashMap<>();

    public Message() {}

    public Message(String topic, byte[] payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public Message(String topic, byte[] payload, Map<String, Object> headers) {
        this.topic = topic;
        this.payload = payload;
        this.headers = headers;
    }

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

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

}
