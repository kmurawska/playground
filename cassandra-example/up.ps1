docker cp .\cql\ cassandra-node-1:/
docker exec -it cassandra-node-1 cqlsh -f cql/00_create_keyspace.cql;
docker exec -it cassandra-node-1 cqlsh -k weather -f cql/01_create_networks_table.cql;
docker exec -it cassandra-node-1 cqlsh -k weather -f cql/02_create_sensors_by_networks_table.cql;