package com.kmurawska.playground.akka.clusterondocker.message;

import java.io.Serializable;
import java.math.BigInteger;

public class FactorialResult implements Serializable {
    private final String trackingId;
    private final Integer number;
    private final BigInteger result;

    public FactorialResult(String trackingId, Integer number, BigInteger result) {
        this.trackingId = trackingId;
        this.number = number;
        this.result = result;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public Integer getNumber() {
        return number;
    }

    public BigInteger getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "FactorialResult{" +
                "trackingId='" + trackingId + '\'' +
                ", number=" + number +
                ", result=" + result +
                '}';
    }
}