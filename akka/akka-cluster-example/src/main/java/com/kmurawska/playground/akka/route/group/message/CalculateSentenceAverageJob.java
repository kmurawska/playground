package com.kmurawska.playground.akka.route.group.message;

import java.io.Serializable;

public class CalculateSentenceAverageJob implements Serializable {
    private final String trackingId, text;

    public CalculateSentenceAverageJob(String trackingId, String text) {
        this.trackingId = trackingId;
        this.text = text;
    }

    public String[] getWords() {
        return text.split(" ");
    }

    public String getTrackingId() {
        return trackingId;
    }

    public boolean isNotEmpty() {
        return !text.isEmpty();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "CalculateSentenceAverageJob{" +
                "trackingId='" + trackingId + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
