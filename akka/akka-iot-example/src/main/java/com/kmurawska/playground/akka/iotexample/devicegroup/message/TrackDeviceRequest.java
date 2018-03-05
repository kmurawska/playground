package com.kmurawska.playground.akka.iotexample.devicegroup.message;

public class TrackDeviceRequest {
    private final String trackingId, groupId, deviceId;

    public TrackDeviceRequest(String trackingId, String groupId, String deviceId) {
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

    public boolean isFor(String groupId) {
        return this.groupId.equals(groupId);
    }
}
