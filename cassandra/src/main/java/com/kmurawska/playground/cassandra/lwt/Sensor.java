package com.kmurawska.playground.cassandra.lwt;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.utils.UUIDs;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Sensor {
    static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 0x01b21dd213814000L;
    private final UUID sensorId;
    private final String type;
    private BigDecimal value;
    private UUID version;

    Sensor(String type) {
        this.sensorId = UUID.randomUUID();
        this.type = type;
        this.version = UUIDs.timeBased();
    }

    Sensor(Row row) {
        this.sensorId = row.getUUID("sensor_id");
        this.type = row.getString("type");
        this.value = row.getDecimal("value");
        this.version = row.getUUID("version");
    }


    public UUID getSensorId() {
        return sensorId;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Sensor updateValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    public UUID getVersion() {
        return version;
    }

    public Instant getLastRecordedAt() {
        return Instant.ofEpochMilli((this.version.timestamp() - NUM_100NS_INTERVALS_SINCE_UUID_EPOCH) / 10000);
    }
}
