package com.kmurawska.playground.mongodb;

import org.bson.Document;

class AverageTemperatureForCountry {
    private final String countryCode;
    private final double average;

    static AverageTemperatureForCountry fromDocument(Document document) {
        return new AverageTemperatureForCountry(
                document.getString("_id"),
                document.getDouble("value"));
    }

    private AverageTemperatureForCountry(String countryCode, double average) {
        this.countryCode = countryCode;
        this.average = average;
    }

    @Override
    public String toString() {
        return "AverageTemperatureForCountry{" +
                "countryCode='" + countryCode + '\'' +
                ", average=" + average +
                '}';
    }
}
