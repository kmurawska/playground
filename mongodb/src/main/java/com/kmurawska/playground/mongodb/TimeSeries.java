package com.kmurawska.playground.mongodb;

import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

class TimeSeries {
    private final String uuid;
    private final LocalDate recordedAt;
    private final Double value;

    static TimeSeries create(int year, int month, Double value) {
        return new TimeSeries(UUID.randomUUID().toString(), LocalDate.of(year, month, 1), value);
    }

    private TimeSeries(String uuid, LocalDate recordedAt, Double value) {
        this.uuid = uuid;
        this.recordedAt = recordedAt;
        this.value = value;
    }

    static List<TimeSeries> fromDocument(List<Document> documents) {
        return documents.stream()
                .map(TimeSeries::fromDocument)
                .collect(toList());
    }

    static TimeSeries fromDocument(Document document) {
        return new TimeSeries(
                document.getString("uuid"),
                LocalDate.ofInstant(document.getDate("recordedAt").toInstant(), ZoneId.systemDefault()),
                document.getDouble("mean")
        );
    }

    Document toDocument() {
        return new Document()
                .append("uuid", uuid)
                .append("recordedAt", recordedAt)
                .append("value", value);
    }

    @Override
    public String toString() {
        return "TimeSeries{" +
                "uuid='" + uuid + '\'' +
                ", recordedAt=" + recordedAt +
                ", value=" + value +
                '}';
    }
}
