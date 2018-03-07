package com.kmurawska.playground.akka.route.group.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ReceiveTimeout;
import com.kmurawska.playground.akka.route.group.message.CalculateSentenceAverageJob;
import com.kmurawska.playground.akka.route.group.message.SentenceAverageResult;
import com.kmurawska.playground.akka.route.group.message.SentenceStatisticFailed;
import com.kmurawska.playground.akka.route.group.message.WordLengthResult;

import java.util.ArrayList;
import java.util.List;

public class AverageCalculator extends AbstractActor {
    private final List<WordLengthResult> results = new ArrayList<>();
    private final int numberOfWords;
    private final ActorRef replyTo;
    private final CalculateSentenceAverageJob job;

    public AverageCalculator(CalculateSentenceAverageJob job, ActorRef replyTo) {
        this.job = job;
        this.numberOfWords = job.getWords().length;
        this.replyTo = replyTo;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WordLengthResult.class, this::onWordLengthResult)
                .match(ReceiveTimeout.class, this::onReceiveTimeout)
                .build();
    }

    private void onWordLengthResult(WordLengthResult result) {
        results.add(result);
        if (results.size() != numberOfWords) return;

        results.stream().mapToDouble(WordLengthResult::getLength).average().ifPresent(a -> {
            replyTo.tell(new SentenceAverageResult(job.getTrackingId(), a), getSelf());
            context().stop(getSelf());
        });
    }

    private void onReceiveTimeout(ReceiveTimeout timeout) {
        replyTo.tell(new SentenceStatisticFailed(job.getTrackingId(), "Service unavailable, try again later"), getSelf());
        context().stop(getSelf());
    }
}