package com.kmurawska.playground.cassandraexample.ttl;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Session;

import java.util.List;
import java.util.UUID;

import static com.kmurawska.playground.cassandraexample.CassandraConnection.CASSANDRA_CONNECTION;
import static java.util.stream.Collectors.toList;

class TemperatureMeasurementRepository {
    private final Session session;

    TemperatureMeasurementRepository() {
        this.session = CASSANDRA_CONNECTION.getSession();
    }

    List<TemperatureMeasurement> findTemperaturesForWeatherStation(final UUID weatherStationId) {
        BoundStatement statement = new BoundStatement(session.prepare(
                "SELECT * FROM " + Keyspace.TEMPERATURE_TABLE + " WHERE weather_station_id = ?"
        )).bind(weatherStationId);

        return this.session.execute(statement)
                .all()
                .stream()
                .map(TemperatureMeasurement::new)
                .collect(toList());
    }

    void save(final TemperatureMeasurement temperature, final int timeToLiveInSeconds) {
        BoundStatement statement = new BoundStatement(session.prepare(
                "INSERT INTO " + Keyspace.TEMPERATURE_TABLE + " (temperature_measurement_id, weather_station_id, value, recorded_at) VALUES (?, ?, ?, ?) USING TTL ?;"
        )).bind(temperature.getId(), temperature.getWeatherStationId(), temperature.getValue(), temperature.getRecordedAt(), timeToLiveInSeconds);

        this.session.execute(statement);
    }
}