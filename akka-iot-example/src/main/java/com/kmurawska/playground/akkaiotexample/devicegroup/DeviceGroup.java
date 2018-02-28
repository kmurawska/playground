package com.kmurawska.playground.akkaiotexample.devicegroup;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akkaiotexample.device.Device;
import com.kmurawska.playground.akkaiotexample.devicegroup.message.ReplyDeviceList;
import com.kmurawska.playground.akkaiotexample.devicegroup.message.RequestDeviceList;
import com.kmurawska.playground.akkaiotexample.devicegroup.message.RequestTrackDevice;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DeviceGroup extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String groupId;
    private final Map<String, ActorRef> deviceActors = new HashMap<>();

    public static Props props(String groupId) {
        return Props.create(DeviceGroup.class, groupId);
    }

    private DeviceGroup(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public void preStart() {
        log.info("DeviceGroup {} started.", groupId);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RequestTrackDevice.class, this::onRequestTrackDevice)
                .match(RequestDeviceList.class, this::onRequestDeviceList)
                .match(Terminated.class, this::onTerminated)
                .build();
    }

    private void onRequestDeviceList(RequestDeviceList requestDeviceList) {
        getSender().tell(new ReplyDeviceList(requestDeviceList.getTrackingId(), deviceActors.keySet()), getSelf());
    }

    private void onTerminated(Terminated terminated) {
        findDeviceIdFor(terminated.getActor()).ifPresent(d -> {
            log.info("Device actor for {} has been terminated.", d);
            deviceActors.remove(d);
        });
    }

    private Optional<String> findDeviceIdFor(ActorRef actor) {
        return deviceActors.entrySet()
                .stream()
                .filter(e -> actor.equals(e.getValue())).map(Map.Entry::getKey)
                .findFirst();
    }

    private void onRequestTrackDevice(RequestTrackDevice requestTrackDevice) {
        if (!requestTrackDevice.isRequestFor(groupId)) {
            log.warning("Ignoring TrackDevice request for {}. This actor is responsible for {}.", requestTrackDevice.getGroupId(), groupId);
        }

        deviceActors.computeIfPresent(requestTrackDevice.getDeviceId(), (k, v) -> {
            forwardRequestToDeviceActor(v, requestTrackDevice);
            return v;
        });

        deviceActors.computeIfAbsent(requestTrackDevice.getDeviceId(), (k) -> createDeviceActorAndForwardMessage(requestTrackDevice));
    }

    private void forwardRequestToDeviceActor(ActorRef actor, RequestTrackDevice requestTrackDevice) {
        actor.forward(requestTrackDevice, getContext());
    }

    private ActorRef createDeviceActorAndForwardMessage(RequestTrackDevice requestTrackDevice) {
        log.info("Creating device actor for {}", requestTrackDevice.getDeviceId());
        ActorRef deviceActor = getContext().actorOf(Device.props(groupId, requestTrackDevice.getDeviceId()), "device-" + requestTrackDevice.getDeviceId());
        getContext().watch(deviceActor);
        forwardRequestToDeviceActor(deviceActor, requestTrackDevice);
        return deviceActor;
    }

    @Override
    public void postStop() {
        log.info("DeviceGroup {} stopped.", groupId);
    }
}
