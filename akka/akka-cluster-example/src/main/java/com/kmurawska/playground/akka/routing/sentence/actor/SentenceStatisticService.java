package com.kmurawska.playground.akka.routing.sentence.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.FromConfig;
import com.kmurawska.playground.akka.routing.sentence.message.CalculateSentenceAverageJob;
import com.kmurawska.playground.akka.routing.sentence.message.CalculateWordLengthJob;

import java.util.stream.Stream;

public class SentenceStatisticService extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef router = getContext().actorOf(FromConfig.getInstance().props(Props.create(WordLengthCounter.class)), "word-length-worker");

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CalculateSentenceAverageJob.class, CalculateSentenceAverageJob::isNotEmpty, this::onCalculateSentenceAverageJob)
                .build();
    }

    private void onCalculateSentenceAverageJob(CalculateSentenceAverageJob job) {
        log.info("----" + job);
        ActorRef averageCalculator = getContext().actorOf(Props.create(AverageCalculator.class, job, getSender()));
        Stream.of(job.getWords())
                .forEach(w -> router.tell(new CalculateWordLengthJob(job.getTrackingId(), w), averageCalculator));
    }
}
