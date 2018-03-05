package com.kmurawska.playground.akka.iotexample.device.message;

public class RecordTemperature {
    private final double value;
    private final String trackingId;

    public RecordTemperature(String trackingId, double value) {
        this.trackingId = trackingId;
        this.value = value;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public double getValue() {
        return value;
    }
}