package com.kmurawska.playground.akka.transformation.message;

import java.io.Serializable;

public class TransformationFailed implements Serializable {
    private final String text;
    private final TransformationJob job;

    public TransformationFailed(String text, TransformationJob job) {
        this.text = text;
        this.job = job;
    }

    public String getText() {
        return text;
    }

    public TransformationJob getJob() {
        return job;
    }

    @Override
    public String toString() {
        return "TransformationFailed{" +
                "text='" + text + '\'' +
                ", job=" + job +
                '}';
    }
}
