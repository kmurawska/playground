package com.kmurawska.playground.akka.adaptiveloadbalancing.message;

import java.io.Serializable;

public class FactorialJob implements Serializable {
    private final String trackingId;
    private final Integer number;

    public FactorialJob(String trackingId, Integer number) {
        this.trackingId = trackingId;
        this.number = number;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public Integer getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "FactorialJob{" +
                "trackingId='" + trackingId + '\'' +
                ", number=" + number +
                '}';
    }
}
