package com.kmurawska.playground.akkaiotexample.devicegroup.message;

public class RequestTrackDevice {
    private final String trackingId, groupId, deviceId;

    public RequestTrackDevice(String trackingId, String groupId, String deviceId) {
        this.trackingId = trackingId;
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}
