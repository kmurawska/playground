package com.kmurawska.playground.akka.iotexample.devicegroup.message;

import com.kmurawska.playground.akka.iotexample.device.DeviceActor;

import java.util.Set;
import java.util.stream.Collectors;

public class ResponseDeviceList {
    private final String trackingId;
    private final Set<DeviceActor> deviceActors;

    public ResponseDeviceList(String trackingId, Set<DeviceActor> deviceActors) {
        this.trackingId = trackingId;
        this.deviceActors = deviceActors;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public Set<String> getDeviceIds() {
        return deviceActors.stream().map(DeviceActor::getDeviceId).collect(Collectors.toSet());
    }
}
