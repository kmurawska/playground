package com.kmurawska.playground.akka.iotexample.alltemperaturesquery.message;

public class AllTemperaturesRequest {
    private final String trackingId;

    public AllTemperaturesRequest(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getTrackingId() {
        return trackingId;
    }
}
