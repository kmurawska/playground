package com.kmurawska.playground.akkaiotexample.devicegroup.message;

import java.util.Set;

public class ReplyDeviceList {
    private final String trackingId;
    private final Set<String> devices;

    public ReplyDeviceList(String trackingId, Set<String> devices) {
        this.trackingId = trackingId;
        this.devices = devices;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public Set<String> getDevices() {
        return devices;
    }
}
