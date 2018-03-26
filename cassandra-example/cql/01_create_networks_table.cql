CREATE TABLE networks (
	network_id UUID,
	name TEXT,
	description TEXT,
	region TEXT,
	number_of_sensors INT,
	PRIMARY KEY ((network_id), name)
);