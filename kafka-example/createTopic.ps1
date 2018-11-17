docker exec -it kafka1 bash -c '$KAFKA_HOME/bin/kafka-topics.sh --create --topic fast-messages --partitions 3 --zookeeper $KAFKA_ZOOKEEPER_CONNECT --replication-factor 2'