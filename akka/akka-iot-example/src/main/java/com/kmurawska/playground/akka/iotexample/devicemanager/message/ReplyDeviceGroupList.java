package com.kmurawska.playground.akka.iotexample.devicemanager.message;

import java.util.Set;

public class ReplyDeviceGroupList {
    private final String trackingId;
    private final Set<String> deviceGroups;

    public ReplyDeviceGroupList(String trackingId, Set<String> deviceGroups) {
        this.trackingId = trackingId;
        this.deviceGroups = deviceGroups;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public Set<String> getDeviceGroups() {
        return deviceGroups;
    }
}
