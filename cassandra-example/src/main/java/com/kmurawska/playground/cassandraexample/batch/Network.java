package com.kmurawska.playground.cassandraexample.batch;

import com.datastax.driver.core.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Network {
    private final UUID networkId;
    private final String name, description, region;
    private final List<Sensor> sensors = new ArrayList<>();

    public Network(Row row) {
        this.networkId = row.getUUID("network_id");
        this.name = row.getString("name");
        this.description = row.getString("description");
        this.region = row.getString("region");
    }

    public Network(UUID uuid, String name, String description, String region) {
        this.networkId = uuid;
        this.name = name;
        this.description = description;
        this.region = region;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRegion() {
        return region;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void addSensor(final String code) {
        Sensor sensor = new Sensor(this.networkId, code);
        this.sensors.add(sensor);
    }
}