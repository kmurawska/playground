package com.kmurawska.playground.akka.routing.sentence.actor;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akka.routing.sentence.message.CalculateWordLengthJob;
import com.kmurawska.playground.akka.routing.sentence.message.WordLengthResult;

public class WordLengthCounter extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CalculateWordLengthJob.class, this::onWordToCount)
                .build();
    }

    private void onWordToCount(CalculateWordLengthJob job) {
        log.info("---" + getSelf().path().name() + " " + job);
        getSender().tell(new WordLengthResult(job.getTrackingId(), job.getText().length()), getSelf());
    }
}