package com.kmurawska.playground.akka.routing.sentence.actor;

import akka.actor.AbstractActor;
import com.kmurawska.playground.akka.routing.sentence.message.CalculateWordLengthJob;
import com.kmurawska.playground.akka.routing.sentence.message.WordLengthResult;

public class WordLengthCounter extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CalculateWordLengthJob.class, this::onWordToCount)
                .build();
    }

    private void onWordToCount(CalculateWordLengthJob job) {
        getSender().tell(new WordLengthResult(job.getTrackingId(), job.getText().length()), getSelf());
    }
}