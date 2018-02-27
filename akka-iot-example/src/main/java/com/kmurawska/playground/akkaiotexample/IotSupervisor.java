package com.kmurawska.playground.akkaiotexample;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class IotSupervisor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(IotSupervisor.class);
    }

    @Override
    public void preStart() {
        log.info("IoT Application started.");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }

    @Override
    public void postStop() throws Exception {
        log.info("IoT application stopped.");
    }
}
