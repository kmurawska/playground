package com.kmurawska.playground.akka.route.group.actor;

import akka.actor.AbstractActor;
import com.kmurawska.playground.akka.route.group.message.CalculateWordLengthJob;
import com.kmurawska.playground.akka.route.group.message.WordLengthResult;

public class WordLengthCounter extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CalculateWordLengthJob.class, this::onWordToCount)
                .build();
    }

    private void onWordToCount(CalculateWordLengthJob word) {
        getSender().tell(new WordLengthResult(word.getText().length()), getSelf());
    }
}