package com.kmurawska.playground.akka.route.group.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.FromConfig;
import com.kmurawska.playground.akka.route.group.message.CalculateSentenceAverageJob;
import com.kmurawska.playground.akka.route.group.message.CalculateWordLengthJob;

import java.util.stream.Stream;

public class SentenceStatisticService extends AbstractActor {
    private ActorRef router = getContext().actorOf(FromConfig.getInstance().props(Props.create(WordLengthCounter.class)), "worker-router");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CalculateSentenceAverageJob.class, CalculateSentenceAverageJob::isNotEmpty, this::onCalculateSentenceAverageJob)
                .build();
    }

    private void onCalculateSentenceAverageJob(CalculateSentenceAverageJob job) {
        ActorRef averageCalculator = getContext().actorOf(Props.create(AverageCalculator.class, job, getSender()));
        Stream.of(job.getWords())
                .forEach(w -> router.tell(new CalculateWordLengthJob(w), averageCalculator));
    }
}
