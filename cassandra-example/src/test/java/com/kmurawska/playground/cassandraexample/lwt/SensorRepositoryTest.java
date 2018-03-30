package com.kmurawska.playground.cassandraexample.lwt;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class SensorRepositoryTest {
    private static final BigDecimal VALUE_UPDATE_1 = new BigDecimal("10.89");
    private static final BigDecimal VALUE_UPDATE_2 = new BigDecimal("18.90");
    private SensorsRepository repository;
    private Sensor sensor;

    @BeforeAll
    static void initAll() {
        new Keyspace().setUp();
    }

    @BeforeEach
    void init() {
        repository = new SensorsRepository();
        sensor = new Sensor("temperature");
        repository.save(sensor);
    }

    @Test
    @DisplayName("Update the latest version of the sensor should be successful")
    void updateOfTheLatestVersionOfProductShouldBeSuccessful() {
        sensor = sensor.updateValue(VALUE_UPDATE_1);
        repository.update(sensor);

        sensor = repository.findByUUID(sensor.getSensorId());

        sensor = sensor.updateValue(VALUE_UPDATE_2);
        repository.update(sensor);

        sensor = repository.findByUUID(sensor.getSensorId());

        assertEquals(VALUE_UPDATE_2, sensor.getValue());
    }

    @Test
    @DisplayName("Update the old version of the sensor should results in optimistic lock violation")
    void updateOfOldVersionOfProductShouldResultInOptimisticLockViolation() {
        sensor = sensor.updateValue(VALUE_UPDATE_1);
        repository.update(sensor);

        assertThrows(IllegalStateException.class, () -> {
            sensor = sensor.updateValue(new BigDecimal("18.90"));
            repository.update(sensor);
        }, "Row has been added by another user.");

        sensor = repository.findByUUID(sensor.getSensorId());

        assertEquals(VALUE_UPDATE_1, sensor.getValue());

        System.out.println(Date.from(sensor.getLastRecordedAt()));
    }

    @AfterAll
    static void cleanUp() {
        new Keyspace().teardown();
    }
}