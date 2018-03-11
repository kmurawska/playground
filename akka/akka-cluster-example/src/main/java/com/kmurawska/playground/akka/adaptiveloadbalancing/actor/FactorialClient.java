package com.kmurawska.playground.akka.adaptiveloadbalancing.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.FromConfig;
import com.kmurawska.playground.akka.adaptiveloadbalancing.message.FactorialJob;
import com.kmurawska.playground.akka.adaptiveloadbalancing.message.FactorialResult;
import com.kmurawska.playground.akka.routing.sentence.actor.WordLengthCounter;
import scala.concurrent.duration.Duration;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FactorialClient extends AbstractActor {
    private final int upToN;
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef router = getContext().actorOf(FromConfig.getInstance().props(Props.create(WordLengthCounter.class)), "factorial-router");

    public FactorialClient(int upToN) {
        this.upToN = upToN;
    }

    @Override
    public void preStart() {
        sendJobs();
        getContext().setReceiveTimeout(Duration.create(10, TimeUnit.SECONDS));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FactorialResult.class, this::onFactorialResult)
                .match(ReceiveTimeout.class, this::onReceiveTimeout)
                .build();
    }

    private void onFactorialResult(FactorialResult result) {
        log.info("{}: {}! = {}", result.getTrackingId(), result.getNumber(), result.getResult());
        if (result.getNumber() == upToN) {
            sendJobs();
        }
    }

    private void onReceiveTimeout(ReceiveTimeout timeout) {
        log.info("Timeout");
        sendJobs();
    }

    private void sendJobs() {
        String trackingId = UUID.randomUUID().toString();
        log.info("Starting batch of factorials up to [{}]", upToN);
        for (int n = 1; n <= upToN; n++) {
            router.tell(new FactorialJob(trackingId, n), self());
        }
    }
}