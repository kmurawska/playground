package com.kmurawska.playground.akkaiotexample.device.message;

import java.util.Optional;

public class RespondTemperature {
    private final String trackingId;
    private final Optional<Double> value;

    public RespondTemperature(String trackingId, Optional<Double> value) {
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
