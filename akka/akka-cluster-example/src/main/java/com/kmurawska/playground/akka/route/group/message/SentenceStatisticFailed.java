package com.kmurawska.playground.akka.route.group.message;

import java.io.Serializable;

public class SentenceStatisticFailed implements Serializable {
    private final String reason;
    private final CalculateSentenceAverageJob job;

    public SentenceStatisticFailed(String reason, CalculateSentenceAverageJob job) {
        this.reason = reason;
        this.job = job;
    }

    @Override
    public String toString() {
        return "SentenceStatisticFailed{" +
                "reason='" + reason + '\'' +
                ", job=" + job +
                '}';
    }
}
