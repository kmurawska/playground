package com.kmurawska.playground.akka.iotexample.devicegroup;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.AllTemperaturesQuery;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.message.AllTemperaturesRequest;
import com.kmurawska.playground.akka.iotexample.device.Device;
import com.kmurawska.playground.akka.iotexample.device.DeviceActor;
import com.kmurawska.playground.akka.iotexample.devicegroup.message.DeviceListRequest;
import com.kmurawska.playground.akka.iotexample.devicegroup.message.ResponseDeviceList;
import com.kmurawska.playground.akka.iotexample.devicegroup.message.TrackDeviceRequest;
import scala.concurrent.duration.FiniteDuration;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toMap;

public class DeviceGroup extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String groupId;
    private final Map<String, ActorRef> deviceActors = new HashMap<>();
    private final Set<DeviceActor> a = new HashSet<>();

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
                .match(TrackDeviceRequest.class, this::onTrackDeviceRequest)
                .match(DeviceListRequest.class, this::onDeviceListRequest)
                .match(AllTemperaturesRequest.class, this::onAllTemperaturesRequest)
                .match(Terminated.class, this::onTerminated)
                .build();
    }

    private void onTrackDeviceRequest(TrackDeviceRequest request) {
        if (!request.isFor(groupId)) {
            log.warning("Ignoring TrackDevice request for {}. This actor is responsible for {}.", request.getGroupId(), groupId);
            return;
        }

        DeviceActor deviceActor = a.stream()
                .filter(d -> d.getDeviceId().equals(request.getDeviceId()))
                .findFirst()
                .orElseGet(() -> createDeviceActorAndForwardMessage(request));

        deviceActor.getActorRef().forward(request, getContext());
    }

    private DeviceActor createDeviceActorAndForwardMessage(TrackDeviceRequest request) {
        log.info("Creating device actor for {}", request.getDeviceId());
        ActorRef actor = getContext().actorOf(Device.props(groupId, request.getDeviceId()), "device-" + request.getDeviceId());
        getContext().watch(actor);
        DeviceActor deviceActor = new DeviceActor(request.getDeviceId(), actor);
        a.add(deviceActor);
        return deviceActor;
    }

    private void onDeviceListRequest(DeviceListRequest request) {
        getSender().tell(new ResponseDeviceList(request.getTrackingId(), a), getSelf());
    }

    private void onAllTemperaturesRequest(AllTemperaturesRequest allTemperaturesRequest) {
        Map<ActorRef, String> devices = deviceActors.entrySet().stream().collect(toMap(Map.Entry::getValue, Map.Entry::getKey));

        getContext().actorOf(AllTemperaturesQuery.props(new HashSet<DeviceActor>(a), allTemperaturesRequest.getTrackingId(), getSender(), new FiniteDuration(3, TimeUnit.SECONDS)));
    }

    private void onTerminated(Terminated terminated) {
        findDeviceActorFor(terminated.getActor()).ifPresent(d -> {
            log.info("Device actor for {} has been terminated.", d);
            a.remove(d);
        });
    }

    private Optional<DeviceActor> findDeviceActorFor(ActorRef deviceActor) {
        return a.stream()
                .filter(e -> deviceActor.equals(e.getActorRef()))
                .findFirst();
    }

    @Override
    public void postStop() {
        log.info("DeviceGroup {} stopped.", groupId);
    }
}