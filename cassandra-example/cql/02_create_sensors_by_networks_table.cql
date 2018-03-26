CREATE TABLE sensors_by_network (
	network_id UUID,
	sensor_id UUID,
	location TEXT,
	PRIMARY KEY ((network_id), sensor_id)
);