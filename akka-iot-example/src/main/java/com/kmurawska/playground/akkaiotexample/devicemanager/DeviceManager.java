package com.kmurawska.playground.akkaiotexample.devicemanager;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akkaiotexample.devicegroup.DeviceGroup;
import com.kmurawska.playground.akkaiotexample.devicegroup.message.RequestTrackDevice;
import com.kmurawska.playground.akkaiotexample.devicemanager.message.ReplyDeviceGroupList;
import com.kmurawska.playground.akkaiotexample.devicemanager.message.RequestDeviceGroupList;

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
                .match(RequestTrackDevice.class, this::onTrackDevice)
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

    private void onTrackDevice(RequestTrackDevice requestTrackDevice) {
        deviceGroupActors.computeIfPresent(requestTrackDevice.getGroupId(), (k, v) -> {
            forwardRequestToDeviceGroupActor(v, requestTrackDevice);
            return v;
        });

        deviceGroupActors.computeIfAbsent(requestTrackDevice.getGroupId(), (k) -> createDeviceActorAndForwardMessage(requestTrackDevice));
    }

    private void forwardRequestToDeviceGroupActor(ActorRef actor, RequestTrackDevice requestTrackDevice) {
        actor.forward(requestTrackDevice, getContext());
    }

    private ActorRef createDeviceActorAndForwardMessage(RequestTrackDevice requestTrackDevice) {
        ActorRef deviceGroupActor = getContext().actorOf(DeviceGroup.props(requestTrackDevice.getGroupId()), "group-" + requestTrackDevice.getGroupId());
        getContext().watch(deviceGroupActor);
        forwardRequestToDeviceGroupActor(deviceGroupActor, requestTrackDevice);
        return deviceGroupActor;
    }

    @Override
    public void postStop() {
        log.info("DeviceManager stopped.");
    }
}
