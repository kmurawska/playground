package com.kmurawska.playground.akka.iotexample.device.message;

import java.util.Optional;

public class DeviceTemperatureResponse {
    private final String trackingId;
    private final Optional<Double> value;

    public DeviceTemperatureResponse(String trackingId, Optional<Double> value) {
        this.trackingId = trackingId;
        this.value = value;
    }

    public Optional<Double> getValue() {
        return value;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
