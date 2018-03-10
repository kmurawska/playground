package com.kmurawska.playground.akka.routing.sentence.message;

import java.io.Serializable;

public class SentenceStatisticFailed implements Serializable {
    private final String reason, trackingId;

    public SentenceStatisticFailed(String reason, String trackingId) {
        this.reason = reason;
        this.trackingId = trackingId;
    }

    @Override
    public String toString() {
        return "SentenceStatisticFailed{" +
                "reason='" + reason + '\'' +
                ", trackingId='" + trackingId + '\'' +
                '}';
    }
}
