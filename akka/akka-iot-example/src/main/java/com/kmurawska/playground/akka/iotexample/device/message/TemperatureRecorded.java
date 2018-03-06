package com.kmurawska.playground.akka.iotexample.device.message;

public class TemperatureRecorded {
    private final String trackingId;

    public TemperatureRecorded(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTrackingId() {
        return trackingId;
    }
}