package com.kmurawska.playground.cassandraexample.lwt;

import com.datastax.driver.core.Session;

import static com.kmurawska.playground.cassandraexample.CassandraConnection.CASSANDRA_CONNECTION;

public class Keyspace {
    private static final String KEYSPACE = "lwt";
    static final String PRODUCTS_TABLE = "lwt.products";
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
                "CREATE TABLE IF NOT EXISTS " + PRODUCTS_TABLE + " (" +
                        "product_id UUID," +
                        "name TEXT," +
                        "description TEXT," +
                        "price DECIMAL," +
                        "version TIMEUUID," +
                        "PRIMARY KEY ((product_id), name)" +
                        ");"
        );
    }

    public void teardown() {
        dropKeyspace();
        dropTables();
    }

    private void dropTables() {
        this.session.execute(
                "DROP TABLE IF EXISTS " + PRODUCTS_TABLE
        );
    }

    private void dropKeyspace() {
        this.session.execute(
                "DROP KEYSPACE IF EXISTS " + KEYSPACE
        );
    }
}