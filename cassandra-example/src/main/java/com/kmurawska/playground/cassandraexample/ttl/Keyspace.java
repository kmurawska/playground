package com.kmurawska.playground.cassandraexample.ttl;

import com.datastax.driver.core.Session;

import static com.kmurawska.playground.cassandraexample.CassandraConnection.CASSANDRA_CONNECTION;

public class Keyspace {
    private static final String KEYSPACE = "ttl";
    static final String TEMPERATURE_TABLE = "ttl.temperature_measurement";
    private final Session session;

    Keyspace() {
        this.session = CASSANDRA_CONNECTION.getSession();
    }

    public void setUp() {
        createKeyspace();
        createTables();
    }

    private void createKeyspace() {
        this.session.execute(
                "CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE + " WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '3' }"
        );
    }

    private void createTables() {
        this.session.execute(
                "CREATE TABLE IF NOT EXISTS " + TEMPERATURE_TABLE + " (" +
                        "temperature_measurement_id UUID," +
                        "weather_station_id UUID," +
                        "value DECIMAL," +
                        "recorded_at TIMESTAMP," +
                        "PRIMARY KEY ((weather_station_id), recorded_at)" +
                        ") WITH CLUSTERING ORDER BY (recorded_at DESC);"
        );
    }

    public void teardown() {
        dropKeyspace();
        dropTables();
    }

    private void dropTables() {
        this.session.execute(
                "DROP TABLE IF EXISTS " + TEMPERATURE_TABLE
        );
    }

    private void dropKeyspace() {
        this.session.execute(
                "DROP KEYSPACE IF EXISTS " + KEYSPACE
        );
    }
}