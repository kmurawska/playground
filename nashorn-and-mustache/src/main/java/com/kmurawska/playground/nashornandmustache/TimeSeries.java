package com.kmurawska.playground.nashornandmustache;

import java.time.LocalDate;
import java.util.UUID;

class TimeSeries {
    private final String uuid;
    private final String recordedAt;
    private final Double value;
    private final String code;

    static TimeSeries create(int year, int month, Double value, String code) {
        return new TimeSeries(UUID.randomUUID().toString(), LocalDate.of(year, month, 1), value, code);
    }

    private TimeSeries(String uuid, LocalDate recordedAt, Double value, String code) {
        this.uuid = uuid;
        this.recordedAt = recordedAt.toString();
        this.value = value;
        this.code = code;
    }
}
