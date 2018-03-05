package com.kmurawska.playground.akka.iotexample.devicemanager;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akka.iotexample.devicemanager.message.ReplyDeviceGroupList;
import com.kmurawska.playground.akka.iotexample.devicemanager.message.RequestDeviceGroupList;
import com.kmurawska.playground.akka.iotexample.devicegroup.DeviceGroup;
import com.kmurawska.playground.akka.iotexample.devicegroup.message.TrackDeviceRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DeviceManager extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final Map<String, ActorRef> deviceGroupActors = new HashMap<>();

    public static Props props() {
        return Props.create(DeviceManager.class);
    }

    private DeviceManager() {
    }

    @Override
    public void preStart() {
        log.info("DeviceManager started.");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(TrackDeviceRequest.class, this::onTrackDevice)
                .match(RequestDeviceGroupList.class, this::onRequestDeviceGroupList)
                .match(Terminated.class, this::onTerminated)
                .build();
    }

    private void onRequestDeviceGroupList(RequestDeviceGroupList requestDeviceGroupList) {
        getSender().tell(new ReplyDeviceGroupList(requestDeviceGroupList.getTrackingId(), deviceGroupActors.keySet()), getSelf());
    }

    private void onTerminated(Terminated terminated) {
        findGroupIdFor(terminated.getActor()).ifPresent(d -> {
            log.info("Device group actor for {} has been terminated.", d);
            deviceGroupActors.remove(d);
        });
    }

    private Optional<String> findGroupIdFor(ActorRef actor) {
        return deviceGroupActors.entrySet()
                .stream()
                .filter(e -> actor.equals(e.getValue())).map(Map.Entry::getKey)
                .findFirst();
    }

    private void onTrackDevice(TrackDeviceRequest trackDeviceRequest) {
        deviceGroupActors.computeIfPresent(trackDeviceRequest.getGroupId(), (k, v) -> {
            forwardRequestToDeviceGroupActor(v, trackDeviceRequest);
            return v;
        });

        deviceGroupActors.computeIfAbsent(trackDeviceRequest.getGroupId(), (k) -> createDeviceActorAndForwardMessage(trackDeviceRequest));
    }

    private void forwardRequestToDeviceGroupActor(ActorRef actor, TrackDeviceRequest trackDeviceRequest) {
        actor.forward(trackDeviceRequest, getContext());
    }

    private ActorRef createDeviceActorAndForwardMessage(TrackDeviceRequest trackDeviceRequest) {
        ActorRef deviceGroupActor = getContext().actorOf(DeviceGroup.props(trackDeviceRequest.getGroupId()), "group-" + trackDeviceRequest.getGroupId());
        getContext().watch(deviceGroupActor);
        forwardRequestToDeviceGroupActor(deviceGroupActor, trackDeviceRequest);
        return deviceGroupActor;
    }

    @Override
    public void postStop() {
        log.info("DeviceManager stopped.");
    }
}
