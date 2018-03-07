package com.kmurawska.playground.akka.route.group.message;

import java.io.Serializable;

public class WordLengthResult implements Serializable {
    private final String trackingId;
    private final double length;

    public WordLengthResult(String trackingId, double length) {
        this.trackingId = trackingId;
        this.length = length;
    }

    public double getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "WordLengthResult{" +
                "trackingId='" + trackingId + '\'' +
                ", length=" + length +
                '}';
    }
}
