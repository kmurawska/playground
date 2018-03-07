package com.kmurawska.playground.akka.route.group.message;

import akka.routing.ConsistentHashingRouter;

import java.io.Serializable;

public class CalculateWordLengthJob implements Serializable, ConsistentHashingRouter.ConsistentHashable {
    private final String trackingId, text;

    public CalculateWordLengthJob(String trackingId, String text) {
        this.trackingId = trackingId;
        this.text = text;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "CalculateWordLengthJob{" +
                "trackingId='" + trackingId + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public Object consistentHashKey() {
        return text + trackingId;
    }
}