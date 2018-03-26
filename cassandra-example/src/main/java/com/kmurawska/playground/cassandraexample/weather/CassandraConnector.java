package com.kmurawska.playground.cassandraexample.weather;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraConnector {
    static final String WEATHER_KEYSPACE = "weather";

    public static Session openSessionFor(String keyspace) {
        Cluster cluster = Cluster.builder()
                .addContactPoints("localhost")
                .build();
        return cluster.connect(keyspace);
    }
}
