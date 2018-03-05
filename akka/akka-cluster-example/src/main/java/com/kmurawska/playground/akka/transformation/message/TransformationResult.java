package com.kmurawska.playground.akka.transformation.message;

import java.io.Serializable;

public class TransformationResult implements Serializable {
    private final String trackingId, text;

    public TransformationResult(String trackingId, String text) {
        this.trackingId = trackingId;
        this.text = text;
    }

    @Override
    public String toString() {
        return "TransformationResult{" +
                "trackingId='" + trackingId + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
