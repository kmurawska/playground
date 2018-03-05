package com.kmurawska.playground.akka.iotexample.alltemperaturesquery.message;

import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.temperature.DeviceTemperatureResponse;

import java.util.Objects;

public class TemperatureResponse {
    private final String deviceId;
    private final DeviceTemperatureResponse deviceTemperatureResponse;

    public TemperatureResponse(String deviceId, DeviceTemperatureResponse deviceTemperatureResponse) {
        this.deviceTemperatureResponse = deviceTemperatureResponse;
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public DeviceTemperatureResponse getDeviceTemperatureResponse() {
        return deviceTemperatureResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemperatureResponse that = (TemperatureResponse) o;
        return Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(deviceTemperatureResponse, that.deviceTemperatureResponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceId, deviceTemperatureResponse);
    }
}