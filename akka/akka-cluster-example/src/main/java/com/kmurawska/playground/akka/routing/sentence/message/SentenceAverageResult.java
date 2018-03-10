package com.kmurawska.playground.akka.routing.sentence.message;

import java.io.Serializable;

public class SentenceAverageResult implements Serializable {
    private final String trackingId;
    private final double average;

    public SentenceAverageResult(String trackingId, double average) {
        this.trackingId = trackingId;
        this.average = average;
    }

    @Override
    public String toString() {
        return "SentenceAverageResult{" +
                "average=" + average +
                ", trackingId='" + trackingId + '\'' +
                '}';
    }
}