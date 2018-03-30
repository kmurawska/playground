package com.kmurawska.playground.cassandraexample.batch;

import com.datastax.driver.core.Row;

import java.util.UUID;

public class Sensor {
    private final UUID sensorId, networkId;
    private final String code;

    Sensor(UUID networkId, String code) {
        this.sensorId = UUID.randomUUID();
        this.networkId = networkId;
        this.code = code;
    }

    Sensor(Row row) {
        this.sensorId = row.getUUID("sensor_id");
        this.networkId = row.getUUID("network_id");
        this.code = row.getString("code");
    }

    public UUID getSensorId() {
        return sensorId;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public String getCode() {
        return code;
    }
}