package com.kmurawska.playground.cassandraexample.lwt;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;

import java.util.UUID;

import static com.kmurawska.playground.cassandraexample.CassandraConnection.CASSANDRA_CONNECTION;
import static com.kmurawska.playground.cassandraexample.lwt.Keyspace.SENSORS_TABLE;

class SensorsRepository {
    private final Session session;

    SensorsRepository() {
        this.session = CASSANDRA_CONNECTION.getSession();
    }

    Sensor findByUUID(UUID sensorId) {
        BoundStatement statement = new BoundStatement(session.prepare(
                "SELECT * FROM " + SENSORS_TABLE + " WHERE sensor_id = ? "
        )).bind(sensorId);

        ResultSet result = this.session.execute(statement);

        return new Sensor(result.one());
    }

    void save(final Sensor sensor) {
        BoundStatement statement = new BoundStatement(session.prepare(
                "INSERT INTO " + SENSORS_TABLE + " (sensor_id, type, version) VALUES (?, ?, ?) IF NOT EXISTS;"
        )).bind(sensor.getSensorId(), sensor.getType(), sensor.getVersion());

        ResultSet result = this.session.execute(statement);

        if (!result.wasApplied())
            throw new IllegalStateException("Row has been added by another user.");
    }

    void update(final Sensor sensor) {
        BoundStatement statement = new BoundStatement(session.prepare(
                "UPDATE " + SENSORS_TABLE + " SET value = ?, version = ? WHERE sensor_id = ? IF version = ?;"
        )).bind(sensor.getValue(), UUIDs.timeBased(), sensor.getSensorId(), sensor.getVersion());

        ResultSet result = this.session.execute(statement);

        if (!result.wasApplied())
            throw new IllegalStateException("Optimistic Lock - Row has been updated by another user.");
    }
}