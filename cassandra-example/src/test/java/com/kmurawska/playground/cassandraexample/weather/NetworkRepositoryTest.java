package com.kmurawska.playground.cassandraexample.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class NetworkRepositoryTest {
    private NetworkRepository repository;

    @BeforeEach
    void init() {
        repository = new NetworkRepository();
    }

    @Test
    public void insert() {
        UUID networkId = UUID.randomUUID();

        Network network = new Network(networkId, "test_name", "test_desc", "test_region");
        network.addSensor(new Sensor("location_1"));
        network.addSensor(new Sensor("location_2"));
        network.addSensor(new Sensor("location_3"));

        repository.save(network);

        Network result = repository.findByUUID(networkId);

        System.out.println(result);
    }
}