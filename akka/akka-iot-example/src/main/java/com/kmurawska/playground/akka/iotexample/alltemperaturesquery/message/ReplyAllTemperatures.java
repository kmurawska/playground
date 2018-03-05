package com.kmurawska.playground.akka.iotexample.alltemperaturesquery.message;

import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.temperature.DeviceTemperatureResponse;

import java.util.Map;

public class ReplyAllTemperatures {
    private final String trackingId;
    private final Map<String, DeviceTemperatureResponse> temperatures;

    public ReplyAllTemperatures(String trackingId, Map<String, DeviceTemperatureResponse> temperatures) {
        this.trackingId = trackingId;
        this.temperatures = temperatures;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public Map<String, DeviceTemperatureResponse> getTemperatures() {
        return temperatures;
    }
}