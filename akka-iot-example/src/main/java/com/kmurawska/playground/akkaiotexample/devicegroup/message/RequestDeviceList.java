package com.kmurawska.playground.akkaiotexample.devicegroup.message;

public class RequestDeviceList {
    private final String trackingId;

    public RequestDeviceList(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
