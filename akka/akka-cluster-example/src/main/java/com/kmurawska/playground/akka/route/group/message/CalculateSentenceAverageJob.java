package com.kmurawska.playground.akka.route.group.message;

import java.io.Serializable;

public class CalculateSentenceAverageJob implements Serializable {
    private final String text;

    public CalculateSentenceAverageJob(String text) {
        this.text = text;
    }

    public String[] getWords() {
        return text.split(" ");
    }

    public boolean isNotEmpty() {
        return !text.isEmpty();
    }

    @Override
    public String toString() {
        return "CalculateSentenceAverageJob{" +
                "text='" + text + '\'' +
                '}';
    }
}
