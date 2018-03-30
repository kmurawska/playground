package com.kmurawska.playground.cassandraexample.batch;

import com.datastax.driver.core.Session;

import static com.kmurawska.playground.cassandraexample.CassandraConnection.CASSANDRA_CONNECTION;

public class Keyspace {
    private static final String KEYSPACE = "batcha";
    static final String NETWORKS_TABLE = "batcha.networks";
    static final String SENSORS_BY_NETWORK_TABLE = "batcha.sensors_by_network";

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
                "CREATE TABLE " + NETWORKS_TABLE + " (" +
                        "network_id UUID," +
                        "name TEXT," +
                        "description TEXT," +
                        "region TEXT," +
                        "PRIMARY KEY ((network_id), name)" +
                        ");"
        );
        this.session.execute(
                "CREATE TABLE " + SENSORS_BY_NETWORK_TABLE + " (" +
                        "network_id UUID," +
                        "sensor_id UUID," +
                        "code TEXT," +
                        "PRIMARY KEY ((network_id), code)" +
                        ");"
        );
    }

    public void teardown() {
        dropKeyspace();
        dropTables();
    }

    private void dropTables() {
        this.session.execute(
                "DROP TABLE IF EXISTS " + NETWORKS_TABLE
        );
        this.session.execute(
                "DROP TABLE IF EXISTS " + SENSORS_BY_NETWORK_TABLE
        );
    }

    private void dropKeyspace() {
        this.session.execute(
                "DROP KEYSPACE IF EXISTS " + KEYSPACE
        );
    }
}