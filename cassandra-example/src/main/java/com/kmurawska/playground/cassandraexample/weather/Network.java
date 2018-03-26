package com.kmurawska.playground.cassandraexample.weather;

import com.datastax.driver.core.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Network {
    private final UUID networkId;
    private final String name, description, region;
    private final int numberOfSensors;
    private final List<Sensor> sensors = new ArrayList<>();

    public Network(Row row) {
        this.networkId = row.getUUID("network_id");
        this.name = row.getString("name");
        this.description = row.getString("description");
        this.region = row.getString("region");
        this.numberOfSensors = row.getInt("number_of_sensors");
    }

    public Network(UUID uuid, String name, String description, String region) {
        this.networkId = uuid;
        this.name = name;
        this.description = description;
        this.region = region;
        this.numberOfSensors = sensors.size();
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

    public int getNumberOfSensors() {
        return numberOfSensors;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    @Override
    public String toString() {
        return "Network{" +
                "networkId=" + networkId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", region='" + region + '\'' +
                ", numberOfSensors=" + numberOfSensors +
                ", sensors=" + sensors +
                '}';
    }

    public void addSensor(final Sensor sensor) {
        this.sensors.add(sensor);
    }
}