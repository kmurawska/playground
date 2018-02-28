package com.kmurawska.playground.akkaiotexample.device;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akkaiotexample.device.message.*;
import com.kmurawska.playground.akkaiotexample.devicegroup.message.RequestTrackDevice;

import java.util.Optional;

public class Device extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String groupId, deviceId;
    private Optional<Double> lastTemperatureReading = Optional.empty();

    public static Props props(String groupId, String deviceId) {
        return Props.create(Device.class, groupId, deviceId);
    }

    private Device(String groupId, String deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    @Override
    public void preStart() {
        log.info("Device actor {}-{} started", groupId, deviceId);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RequestTrackDevice.class, this::onTrackDeviceRequest)
                .match(ReadTemperature.class, this::onReadTemperature)
                .match(RecordTemperature.class, this::onRecordTemperature)
                .build();
    }

    private void onTrackDeviceRequest(RequestTrackDevice r) {
        if (groupAndDeviceMatch(r)) {
            getSender().tell(new DeviceRegistered(r.getTrackingId()), getSelf());
        } else {
            log.warning("Ignoring TrackDevice request for {}-{}. This actor is responsible for {}-{}", r.getGroupId(), r.getDeviceId(), groupId, deviceId);
        }
    }

    private boolean groupAndDeviceMatch(RequestTrackDevice r) {
        return groupId.equals(r.getGroupId()) && deviceId.equals(r.getDeviceId());
    }

    private void onRecordTemperature(RecordTemperature r) {
        log.info("Recorder temperature reading {} with {}", r.getValue(), r.getTrackingId());
        lastTemperatureReading = Optional.of(r.getValue());
        getSender().tell(new TemperatureRecorded(r.getTrackingId()), getSelf());
    }

    private void onReadTemperature(ReadTemperature r) {
        getSender().tell(new RespondTemperature(r.getTrackingId(), lastTemperatureReading), getSelf());
    }

    @Override
    public void postStop() {
        log.info("Device actor {}-{} stopped", groupId, deviceId);
    }
}
