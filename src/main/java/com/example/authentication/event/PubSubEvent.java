package com.example.authentication.event;

import com.example.authentication.util.Constants;

public class PubSubEvent<T> {
    private String eventType;
    private String timestamp;
    private T data;

    public PubSubEvent() {
    }

    public PubSubEvent(String eventType, String timestamp, T data) {
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.data = data;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
