package com.kmurawska.playground.cassandra.ttl;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemperatureRepositoryTest {
    private static final int TIME_TO_LIVE_IN_SECONDS = 5;
    private TemperatureMeasurementRepository repository;
    private CountDownLatch lock = new CountDownLatch(1);

    @BeforeAll
    static void initAll() {
        new Keyspace().setUp();
    }

    @BeforeEach
    void init() {
        repository = new TemperatureMeasurementRepository();
    }

    @Test
    @DisplayName("Temperature measurement should be removed after five seconds")
    void temperatureMeasurementRecordShouldBeRemovedAfterFiveSeconds() throws InterruptedException {
        UUID weatherStationId = UUID.randomUUID();

        TemperatureMeasurement temperatureMeasurement = new TemperatureMeasurement(weatherStationId, new BigDecimal("10.5"));
        repository.save(temperatureMeasurement, TIME_TO_LIVE_IN_SECONDS);

        TemperatureMeasurement result = repository.findTemperaturesForWeatherStation(weatherStationId).get(0);
        assertEquals(temperatureMeasurement.getId(), result.getId());
        assertEquals(temperatureMeasurement.getWeatherStationId(), result.getWeatherStationId());
        assertEquals(temperatureMeasurement.getValue(), result.getValue());
        assertEquals(temperatureMeasurement.getRecordedAt(), result.getRecordedAt());

        lock.await(TIME_TO_LIVE_IN_SECONDS, TimeUnit.SECONDS);

        assertTrue(repository.findTemperaturesForWeatherStation(weatherStationId).isEmpty());
    }

    @AfterAll
    static void cleanUp() {
        new Keyspace().teardown();
    }
}