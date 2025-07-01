package com.example.authentication.event;

import com.example.authentication.util.Constants;

public class PubSubEvent<T> {
    private String eventType = Constants.EVENT_USER_REGISTERED;
    private String timestamp;
    private T data;

    public PubSubEvent() {
    }

    public PubSubEvent(String timestamp, T data) {
        this.timestamp = timestamp;
        this.data = data;
    }

    public String getEventType() {
        return eventType;
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
