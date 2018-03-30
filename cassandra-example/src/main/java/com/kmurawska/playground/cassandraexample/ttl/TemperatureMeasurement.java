package com.kmurawska.playground.cassandraexample.ttl;

import com.datastax.driver.core.Row;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class TemperatureMeasurement {
    private final UUID weatherStationId, id;
    private final BigDecimal value;
    private final Instant recordedAt;

    TemperatureMeasurement(UUID weatherStationId, BigDecimal value) {
        this.weatherStationId = weatherStationId;
        this.id = UUID.randomUUID();
        this.value = value;
        this.recordedAt = Instant.now();
    }

    TemperatureMeasurement(Row row) {
        this.id = row.getUUID("temperature_measurement_id");
        this.weatherStationId = row.getUUID("weather_station_id");
        this.value = row.getDecimal("value");
        this.recordedAt = row.getTimestamp("recorded_at").toInstant();
    }

    public UUID getWeatherStationId() {
        return weatherStationId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }

    public UUID getId() {
        return id;
    }
}