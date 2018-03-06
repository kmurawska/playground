package com.kmurawska.playground.akka.route.group.message;

import java.io.Serializable;

public class SentenceAverageResult implements Serializable {
    private final double average;

    public SentenceAverageResult(double average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return "SentenceAverageResult{" +
                "averageWordLength=" + average +
                '}';
    }
}