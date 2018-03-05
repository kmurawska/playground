package com.kmurawska.playground.akka.iotexample.devicemanager.message;

public class RequestDeviceGroupList {
    private final String trackingId;

    public RequestDeviceGroupList(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
