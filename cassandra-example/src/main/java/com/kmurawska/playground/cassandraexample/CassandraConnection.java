package com.kmurawska.playground.cassandraexample;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public enum CassandraConnection {
    CASSANDRA_CONNECTION;

    private static final String CONTACT_POINT = "localhost";
    private Cluster cluster;
    private Session session;

    CassandraConnection() {
        if (cluster == null && session == null) {
            cluster = Cluster.builder()
                    .addContactPoints(CONTACT_POINT)
                    .build();
            session = cluster.connect();
        }
    }

    public Session getSession() {
        if (session == null) {
            throw new IllegalStateException("No connection initialized");
        }
        return session;
    }

    public void close() {
        if (null != session) {
            session.close();
        }
        if (null != cluster) {
            cluster.close();
        }
    }
}