package com.kmurawska.playground.akkaiotexample.devicegroupquery;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akkaiotexample.device.message.ReadTemperature;
import com.kmurawska.playground.akkaiotexample.device.message.RespondTemperature;
import scala.concurrent.duration.FiniteDuration;

import java.util.*;

public class DeviceGroupQuery extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final Map<ActorRef, String> deviceActors;
    private final String trackingId;
    private final ActorRef requester;
    private Cancellable timeoutTimer;

    public static Props props(Map<ActorRef, String> deviceActors, String trackingId, ActorRef requester, FiniteDuration timeout) {
        return Props.create(DeviceGroupQuery.class, deviceActors, trackingId, requester, timeout);
    }

    private DeviceGroupQuery(Map<ActorRef, String> deviceActors, String trackingId, ActorRef requester, FiniteDuration timeout) {
        this.deviceActors = deviceActors;
        this.trackingId = trackingId;
        this.requester = requester;

        timeoutTimer = getContext().getSystem().scheduler().scheduleOnce(
                timeout, getSelf(), new DeviceGroupQueryTimeout(), getContext().dispatcher(), getSender()
        );
    }

    @Override
    public void preStart() throws Exception {
        deviceActors.keySet().forEach(a -> {
            getContext().watch(a);
            a.tell(new ReadTemperature(UUID.randomUUID().toString()), getSelf());
        });
    }

    @Override
    public Receive createReceive() {
        return waitingForReplies(new HashMap<>(), deviceActors.keySet());
    }

    private Receive waitingForReplies(Map<String, TemperatureReading> repliesSoFar, Set<ActorRef> stillWaiting) {
        return receiveBuilder()
                .match(RespondTemperature.class, r -> {
                    ActorRef deviceActor = getSender();
                    TemperatureReading temperatureReading = r.getValue()
                            .map(v -> (TemperatureReading) new Temperature(v))
                            .orElse(new TemperatureNotAvailable());
                    receivedResponse(deviceActor, temperatureReading, stillWaiting, repliesSoFar);
                })
                .match(Terminated.class, r -> {
                    receivedResponse(r.getActor(), new DeviceNotAvailable(), stillWaiting, repliesSoFar);
                })
                .match(DeviceGroupQueryTimeout.class, r -> {
                    Map<String, TemperatureReading> replies = new HashMap<>(repliesSoFar);
                    stillWaiting.forEach(a -> {
                        String deviceId = deviceActors.get(a);
                        replies.put(deviceId, new DeviceTimedOut());
                    });
                    requester.tell(new ReplyAllTemperatures(trackingId, replies), getSelf());
                    getContext().stop(getSelf());
                })
                .build();
    }

    private void receivedResponse(ActorRef deviceActor, TemperatureReading temperatureReading, Set<ActorRef> stillWaiting, Map<String, TemperatureReading> repliesSoFar) {
        getContext().unwatch(deviceActor);
        String deviceId = deviceActors.get(deviceActor);
        Set<ActorRef> newStillWaiting = new HashSet<>(stillWaiting);
        newStillWaiting.remove(deviceActor);

        Map<String, TemperatureReading> newRepliesSoFar = new HashMap<>(repliesSoFar);
        newRepliesSoFar.put(deviceId, temperatureReading);

        if (newStillWaiting.isEmpty()) {
            requester.tell(new ReplyAllTemperatures(trackingId, newRepliesSoFar), getSelf());
            getContext().stop(getSelf());
        } else {
            getContext().become(waitingForReplies(newRepliesSoFar, newStillWaiting));
        }

    }

    @Override
    public void postStop() throws Exception {
        timeoutTimer.cancel();
    }
}
