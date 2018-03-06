package com.kmurawska.playground.akka.route.group.message;

import java.io.Serializable;

public class WordLengthResult implements Serializable {
    private final double length;

    public WordLengthResult(double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "WordLengthResult{" +
                "length=" + length +
                '}';
    }
}
