package com.kmurawska.playground.akkaiotexample.devicegroupquery;

public class Temperature implements TemperatureReading {
    private final double value;

    public Temperature(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}