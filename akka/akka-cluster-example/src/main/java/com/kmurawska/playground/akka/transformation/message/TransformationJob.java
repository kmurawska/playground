package com.kmurawska.playground.akka.transformation.message;

import java.io.Serializable;

public class TransformationJob implements Serializable {
    private final String trackingId, text;

    public TransformationJob(String trackingId, String text) {
        this.trackingId = trackingId;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getTrackingId() {
        return trackingId;
    }

    @Override
    public String toString() {
        return "TransformationJob{" +
                "trackingId='" + trackingId + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
