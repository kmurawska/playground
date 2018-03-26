package com.kmurawska.playground.cassandraexample.weather;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Session;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public class NetworkRepository {
    private Session session;

    NetworkRepository() {
        this.session = CassandraConnector.openSessionFor(CassandraConnector.WEATHER_KEYSPACE);
    }

    public List<Network> findAll() {
        return this.session.execute(
                "SELECT * FROM networks").all()
                .stream()
                .map(Network::new)
                .collect(toList());
    }

    public Network findByUUID(UUID videoId) {
        return new Network(
                this.session.execute(
                        "SELECT * FROM networks WHERE network_id = " + videoId
                ).one()
        );
    }

    public void save(final Network network) {
        BatchStatement batch = new BatchStatement()
                .add(statementForNetwork(network))
                .addAll(statementsForSensors(network));

        this.session.execute(batch);
    }

    private BoundStatement statementForNetwork(final Network network) {
        return new BoundStatement(session.prepare(
                "INSERT INTO networks (network_id, name, description, region, number_of_sensors) VALUES (?, ?, ?, ?, ?);"
        )).bind(network.getNetworkId(), network.getName(), network.getDescription(), network.getRegion(), network.getNumberOfSensors());
    }

    private List<BoundStatement> statementsForSensors(final Network network) {
        return network.getSensors()
                .stream()
                .map(s -> statementForSensor(network, s))
                .collect(toList());
    }

    private BoundStatement statementForSensor(final Network network, final Sensor sensor) {
        return new BoundStatement(session.prepare(
                "INSERT INTO sensors_by_network (network_id, sensor_id, location) VALUES (?, ?, ?);"))
                .bind(network.getNetworkId(), sensor.getUuid(), sensor.getLocation());
    }
}