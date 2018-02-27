package com.kmurawska.playground.akkaiotexample.device.message;

public class ReadTemperature {
    private final String trackingId;

    public ReadTemperature(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
