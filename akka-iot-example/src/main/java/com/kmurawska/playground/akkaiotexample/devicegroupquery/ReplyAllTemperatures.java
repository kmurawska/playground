package com.kmurawska.playground.akkaiotexample.devicegroupquery;

import java.util.Map;

public class ReplyAllTemperatures {
    private final String trackingId;
    private final Map<String, TemperatureReading> temperatures;

    public ReplyAllTemperatures(String trackingId, Map<String, TemperatureReading> temperatures) {
        this.trackingId = trackingId;
        this.temperatures = temperatures;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public Map<String, TemperatureReading> getTemperatures() {
        return temperatures;
    }
}