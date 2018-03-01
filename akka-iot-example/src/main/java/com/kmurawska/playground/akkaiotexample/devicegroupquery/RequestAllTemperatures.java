package com.kmurawska.playground.akkaiotexample.devicegroupquery;

public class RequestAllTemperatures {
    private final String trackingId;

    public RequestAllTemperatures(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
