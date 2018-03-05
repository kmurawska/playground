package com.kmurawska.playground.akka.iotexample.devicegroup.message;

public class DeviceListRequest {
    private final String trackingId;

    public DeviceListRequest(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
