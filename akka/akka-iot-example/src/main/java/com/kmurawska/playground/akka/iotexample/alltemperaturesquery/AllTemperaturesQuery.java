package com.kmurawska.playground.akka.iotexample.alltemperaturesquery;

import akka.actor.*;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.message.ReplyAllTemperatures;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.temperature.*;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.temperature.DeviceTemperatureResponse;
import com.kmurawska.playground.akka.iotexample.device.DeviceActor;
import com.kmurawska.playground.akka.iotexample.device.message.ReadTemperature;
import scala.concurrent.duration.FiniteDuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AllTemperaturesQuery extends AbstractActor {
    private final Set<DeviceActor> deviceActors;
    private final String trackingId;
    private final ActorRef requester;
    private final Cancellable timeoutTimer;

    public static Props props(Set<DeviceActor> deviceActors, String trackingId, ActorRef requester, FiniteDuration timeout) {
        return Props.create(AllTemperaturesQuery.class, deviceActors, trackingId, requester, timeout);
    }

    private AllTemperaturesQuery(Set<DeviceActor> deviceActors, String trackingId, ActorRef requester, FiniteDuration timeout) {
        this.deviceActors = deviceActors;
        this.trackingId = trackingId;
        this.requester = requester;
        this.timeoutTimer = getContext().getSystem().scheduler().scheduleOnce(
                timeout, getSelf(), new DeviceGroupQueryTimeout(), getContext().dispatcher(), getSender()
        );
    }

    @Override
    public void preStart() {
        deviceActors.forEach(d -> {
            getContext().watch(d.getActorRef());
            d.getActorRef().tell(new ReadTemperature(trackingId), getSelf());
        });
    }

    @Override
    public Receive createReceive() {
        return waitForResponse(new HashMap<>(), deviceActors);
    }

    private Receive waitForResponse(Map<String, DeviceTemperatureResponse> repliesSoFar, Set<DeviceActor> stillWaiting) {
        return receiveBuilder()
                .match(com.kmurawska.playground.akka.iotexample.device.message.DeviceTemperatureResponse.class, r -> onDeviceTemperatureResponse(r, repliesSoFar, stillWaiting))
                .match(Terminated.class, r -> onTerminated(r, repliesSoFar, stillWaiting))
                .match(DeviceGroupQueryTimeout.class, r -> onDeviceGroupQueryTimeout(repliesSoFar, stillWaiting))
                .build();
    }

    private void onDeviceTemperatureResponse(com.kmurawska.playground.akka.iotexample.device.message.DeviceTemperatureResponse response, Map<String, DeviceTemperatureResponse> repliesSoFar, Set<DeviceActor> stillWaiting) {
        DeviceTemperatureResponse deviceTemperatureResponse = response.getValue()
                .map(v -> (DeviceTemperatureResponse) new DeviceTemperature(v))
                .orElse(new DeviceTemperatureNotAvailable());

        receivedResponse(getSender(), deviceTemperatureResponse, stillWaiting, repliesSoFar);
    }

    private void receivedResponse(ActorRef deviceActor, DeviceTemperatureResponse deviceTemperatureResponse, Set<DeviceActor> stillWaiting, Map<String, DeviceTemperatureResponse> repliesSoFar) {
        deviceActors.stream()
                .filter(d -> d.getActorRef().equals(deviceActor))
                .findFirst().ifPresent(d -> receivedResponse(deviceActor, deviceTemperatureResponse, stillWaiting, repliesSoFar, d));
    }

    private void receivedResponse(ActorRef deviceActor, DeviceTemperatureResponse deviceTemperatureResponse, Set<DeviceActor> stillWaiting, Map<String, DeviceTemperatureResponse> repliesSoFar, DeviceActor d) {
        Set<DeviceActor> newStillWaiting = unwatchDevice(deviceActor, stillWaiting);
        Map<String, DeviceTemperatureResponse> newResponsesSoFar = addResponse(deviceTemperatureResponse, repliesSoFar, d);

        if (newStillWaiting.isEmpty()) {
            requester.tell(new ReplyAllTemperatures(trackingId, newResponsesSoFar), getSelf());
            getContext().stop(getSelf());
        } else {
            getContext().become(waitForResponse(newResponsesSoFar, newStillWaiting));
        }
    }

    private Set<DeviceActor> unwatchDevice(ActorRef deviceActor, Set<DeviceActor> stillWaiting) {
        getContext().unwatch(deviceActor);
        return stillWaiting.stream().filter(a -> !deviceActor.equals(a.getActorRef())).collect(Collectors.toSet());
    }

    private Map<String, DeviceTemperatureResponse> addResponse(DeviceTemperatureResponse deviceTemperatureResponse, Map<String, DeviceTemperatureResponse> repliesSoFar, DeviceActor d) {
        Map<String, DeviceTemperatureResponse> newRepliesSoFar = new HashMap<>(repliesSoFar);
        newRepliesSoFar.put(d.getDeviceId(), deviceTemperatureResponse);
        return newRepliesSoFar;
    }

    private void onTerminated(Terminated terminated, Map<String, DeviceTemperatureResponse> repliesSoFar, Set<DeviceActor> stillWaiting) {
        receivedResponse(terminated.getActor(), new DeviceNotAvailable(), stillWaiting, repliesSoFar);
    }

    private void onDeviceGroupQueryTimeout(Map<String, DeviceTemperatureResponse> repliesSoFar, Set<DeviceActor> stillWaiting) {
        Map<String, DeviceTemperatureResponse> replies = new HashMap<>(repliesSoFar);
        replies.putAll(stillWaiting.stream().collect(Collectors.toMap(DeviceActor::getDeviceId, d -> new DeviceTimedOut())));
        requester.tell(new ReplyAllTemperatures(trackingId, replies), getSelf());
        getContext().stop(getSelf());
    }

    @Override
    public void postStop() {
        timeoutTimer.cancel();
    }
}