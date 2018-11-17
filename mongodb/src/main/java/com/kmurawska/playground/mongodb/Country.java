package com.kmurawska.playground.mongodb;

import org.bson.Document;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

class Country {
    static final Country UNKNOWN_COUNTRY = new Country("", "", "");
    private final String uuid, name, code;
    private List<TimeSeries> temperature;

    static Country create(String name, String code) {
        return new Country(UUID.randomUUID().toString(), name, code);
    }

    private Country(String uuid, String name, String code) {
        this.uuid = uuid;
        this.name = name;
        this.code = code;
    }

    static Country fromDocument(Document document) {
        return new Country(
                document.getString("uuid"),
                document.getString("name"),
                document.getString("code")
        ).withTemperature(TimeSeries.fromDocument((List<Document>) document.get("temperature")));
    }

    Country withTemperature(List<TimeSeries> temperature) {
        this.temperature = temperature;
        return this;
    }

    public List<TimeSeries> getTemperature() {
        return temperature;
    }

    Document toDocument() {
        return new Document()
                .append("uuid", this.uuid)
                .append("name", this.name)
                .append("code", this.code)
                .append("temperature", temperature.stream().map(TimeSeries::toDocument).collect(toList()));
    }

    @Override
    public String toString() {
        return "Country{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", temperature=" + temperature.size() +
                '}';
    }
}