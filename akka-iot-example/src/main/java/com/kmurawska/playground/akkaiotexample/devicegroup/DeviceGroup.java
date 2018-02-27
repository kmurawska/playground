package com.kmurawska.playground.akkaiotexample.devicegroup;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akkaiotexample.device.Device;
import com.kmurawska.playground.akkaiotexample.devicegroup.message.RequestTrackDevice;

import java.util.HashMap;
import java.util.Map;

public class DeviceGroup extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String groupId;
    private final Map<String, ActorRef> devices = new HashMap<>();

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
                .match(RequestTrackDevice.class, this::onTrackDevice)
                .build();
    }

    private void onTrackDevice(RequestTrackDevice requestTrackDevice) {
        if (!groupId.equals(requestTrackDevice.getGroupId())) {
            log.warning("Ignoring TrackDevice request for {}. This actor is responsible for {}.", requestTrackDevice.getGroupId(), groupId);
        }

        devices.computeIfPresent(requestTrackDevice.getDeviceId(), (k, v) -> {
            v.forward(requestTrackDevice, getContext());
            return v;
        });

        devices.computeIfAbsent(requestTrackDevice.getDeviceId(), (k) -> {
            log.info("Creating device actor for {}", requestTrackDevice.getDeviceId());
            ActorRef deviceActor = getContext().actorOf(Device.props(groupId, requestTrackDevice.getDeviceId()), "device-" + requestTrackDevice.getDeviceId());
            deviceActor.forward(requestTrackDevice, getContext());
            return deviceActor;
        });
    }

    @Override
    public void postStop() {
        log.info("DeviceGroup {} stopped.", groupId);
    }
}
