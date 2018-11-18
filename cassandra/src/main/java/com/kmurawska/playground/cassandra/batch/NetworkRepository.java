package com.kmurawska.playground.cassandra.batch;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.List;
import java.util.UUID;

import static com.kmurawska.playground.cassandra.CassandraConnection.CASSANDRA_CONNECTION;
import static com.kmurawska.playground.cassandra.batch.Keyspace.NETWORKS_TABLE;
import static com.kmurawska.playground.cassandra.batch.Keyspace.SENSORS_BY_NETWORK_TABLE;
import static java.util.stream.Collectors.toList;

public class NetworkRepository {
    private final Session session;

    NetworkRepository() {
        this.session = CASSANDRA_CONNECTION.getSession();
    }

    public Network findByUUID(UUID networkId) {
        BoundStatement statement = new BoundStatement(session.prepare(
                "SELECT * FROM " + NETWORKS_TABLE + " WHERE network_id = ? "
        )).bind(networkId);

        ResultSet result = this.session.execute(statement);

        return new Network(result.one());
    }

    public List<Sensor> findSensorsInNetwork(UUID networkId) {
        BoundStatement statement = new BoundStatement(session.prepare(
                "SELECT * FROM " + SENSORS_BY_NETWORK_TABLE + " WHERE network_id = ? "
        )).bind(networkId);

        return this.session.execute(statement).all()
                .stream()
                .map(Sensor::new)
                .collect(toList());
    }

    public void save(final Network network) {
        BatchStatement batch = new BatchStatement()
                .add(statementForNetwork(network))
                .addAll(statementsForSensors(network));

        this.session.execute(batch);
    }

    private BoundStatement statementForNetwork(final Network network) {
        return new BoundStatement(session.prepare(
                "INSERT INTO " + NETWORKS_TABLE + " (network_id, name, description, region) VALUES (?, ?, ?, ?);"
        )).bind(network.getNetworkId(), network.getName(), network.getDescription(), network.getRegion());
    }

    private List<BoundStatement> statementsForSensors(final Network network) {
        return network.getSensors()
                .stream()
                .map(s -> statementForSensor(network, s))
                .collect(toList());
    }

    private BoundStatement statementForSensor(final Network network, final Sensor sensor) {
        return new BoundStatement(session.prepare(
                "INSERT INTO " + SENSORS_BY_NETWORK_TABLE + " (network_id, sensor_id, code) VALUES (?, ?, ?);"))
                .bind(network.getNetworkId(), sensor.getSensorId(), sensor.getCode());
    }
}