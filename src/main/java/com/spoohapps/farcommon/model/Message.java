package com.spoohapps.farcommon.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
        return getHeaders().get(key);
    }

    public void addHeader(String key, Object value) {
        getHeaders().put(key, value);
    }

    public Map<String, Object> getHeaders() {
        if (headers != null) {
            return headers;
        } else {
            headers = new HashMap<>();
            return headers;
        }
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
