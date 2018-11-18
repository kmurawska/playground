package com.kmurawska.playground.cassandra.batch;

import org.junit.jupiter.api.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NetworkRepositoryTest {
    private static final String NAME = "test_name";
    private static final String DESCRIPTION = "test_desc";
    private static final String REGION = "test_region";
    private static final String SENSOR_CODE_1 = "sensor_code_1";
    private static final String SENSOR_CODE_2 = "sensor_code_2";
    private static final String SENSOR_CODE_3 = "sensor_code_3";
    private NetworkRepository repository;

    @BeforeAll
    static void initAll() {
        new Keyspace().setUp();
    }

    @BeforeEach
    void init() {
        repository = new NetworkRepository();
    }

    @Test
    @DisplayName("Save network with sensors")
    void saveNetworkWithSensors() {
        UUID networkId = UUID.randomUUID();

        Network network = new Network(networkId, NAME, DESCRIPTION, REGION);
        network.addSensor(SENSOR_CODE_1);
        network.addSensor(SENSOR_CODE_2);
        network.addSensor(SENSOR_CODE_3);

        repository.save(network);

        Network result = repository.findByUUID(networkId);

        assertEquals(networkId, network.getNetworkId());
        assertEquals(NAME, network.getName());
        assertEquals(DESCRIPTION, network.getDescription());
        assertEquals(REGION, network.getRegion());

        List<Sensor> sensorsInNetwork = repository.findSensorsInNetwork(result.getNetworkId());

        assertEquals(3, sensorsInNetwork.size());
        assertEquals(network.getNetworkId(), sensorsInNetwork.get(0).getNetworkId());
        assertEquals(SENSOR_CODE_1, sensorsInNetwork.get(0).getCode());
        assertEquals(network.getNetworkId(), sensorsInNetwork.get(1).getNetworkId());
        assertEquals(SENSOR_CODE_2, sensorsInNetwork.get(1).getCode());
        assertEquals(network.getNetworkId(), sensorsInNetwork.get(2).getNetworkId());
        assertEquals(SENSOR_CODE_3, sensorsInNetwork.get(2).getCode());
    }

    @AfterAll
    static void cleanUp() {
        new Keyspace().teardown();
    }
}