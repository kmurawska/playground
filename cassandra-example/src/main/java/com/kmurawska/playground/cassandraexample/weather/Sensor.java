package com.kmurawska.playground.cassandraexample.weather;

import java.util.UUID;

public class Sensor {
    private final UUID uuid;
    private final String location;

    public Sensor(String location) {
        this.uuid = UUID.randomUUID();
        this.location = location;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getLocation() {
        return location;
    }
}
