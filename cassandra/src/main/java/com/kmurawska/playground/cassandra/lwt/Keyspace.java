package com.kmurawska.playground.cassandra.lwt;

import com.datastax.driver.core.Session;

import static com.kmurawska.playground.cassandra.CassandraConnection.CASSANDRA_CONNECTION;

public class Keyspace {
    private static final String KEYSPACE = "lwt";
    static final String SENSORS_TABLE = "lwt.sensors";
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
                "CREATE TABLE IF NOT EXISTS " + SENSORS_TABLE + " (" +
                        "sensor_id UUID," +
                        "type TEXT," +
                        "value DECIMAL," +
                        "version TIMEUUID," +
                        "PRIMARY KEY (sensor_id)" +
                        ");"
        );
    }

    public void teardown() {
        dropKeyspace();
        dropTables();
    }

    private void dropTables() {
        this.session.execute(
                "DROP TABLE IF EXISTS " + SENSORS_TABLE
        );
    }

    private void dropKeyspace() {
        this.session.execute(
                "DROP KEYSPACE IF EXISTS " + KEYSPACE
        );
    }
}