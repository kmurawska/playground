package com.kmurawska.playground.akka.iotexample.alltemperaturesquery.temperature;

public class DeviceTemperature implements DeviceTemperatureResponse {
    private final Double value;

    public DeviceTemperature(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}
