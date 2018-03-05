package com.kmurawska.playground.akka.iotexample;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akka.iotexample.devicemanager.message.ReplyDeviceGroupList;

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
        return receiveBuilder()
                .match(ReplyDeviceGroupList.class, r -> r.getDeviceGroups().forEach(d -> log.info("- " + d)))
                .build();
    }

    @Override
    public void postStop() {
        log.info("IoT application stopped.");
    }
}