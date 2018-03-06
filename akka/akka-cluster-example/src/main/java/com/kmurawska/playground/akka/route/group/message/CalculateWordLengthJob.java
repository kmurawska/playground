package com.kmurawska.playground.akka.route.group.message;

import akka.routing.ConsistentHashingRouter;

import java.io.Serializable;
import java.util.Objects;

public class CalculateWordLengthJob implements Serializable, ConsistentHashingRouter.ConsistentHashable {
    private final String text;

    public CalculateWordLengthJob(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalculateWordLengthJob that = (CalculateWordLengthJob) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public String toString() {
        return "CalculateWordLengthJob{" +
                "text='" + text + '\'' +
                '}';
    }

    @Override
    public Object consistentHashKey() {
        return text;
    }
}