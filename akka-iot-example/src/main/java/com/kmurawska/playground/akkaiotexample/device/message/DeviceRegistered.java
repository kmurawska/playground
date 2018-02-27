package com.kmurawska.playground.akkaiotexample.device.message;

public class DeviceRegistered {
    private final String trackingId;

    public DeviceRegistered(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
