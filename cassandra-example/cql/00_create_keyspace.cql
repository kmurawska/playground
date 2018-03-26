CREATE KEYSPACE weather WITH REPLICATION = {
  'class':'SimpleStrategy',
  'replication_factor':3
};