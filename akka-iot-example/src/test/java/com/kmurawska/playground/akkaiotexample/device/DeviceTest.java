package com.kmurawska.playground.akkaiotexample.device;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.kmurawska.playground.akkaiotexample.device.message.*;
import com.kmurawska.playground.akkaiotexample.devicegroup.message.RequestTrackDevice;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceTest {
    private static final String DEVICE_ID = "device";
    private static final String GROUP_ID = "group";
    private static ActorSystem system;
    private TestKit probe;
    private ActorRef deviceActor;

    @BeforeAll
    static void init() {
        system = ActorSystem.create();
    }

    @BeforeEach
    void initTest() {
        probe = new TestKit(system);
        deviceActor = system.actorOf(Device.props("group", "device"));

    }

    @Test
    void replyToValidRegistrationRequest() {
        String trackingId = UUID.randomUUID().toString();

        deviceActor.tell(new RequestTrackDevice(trackingId, GROUP_ID, DEVICE_ID), probe.getRef());

        probe.expectMsgClass(DeviceRegistered.class);
        assertEquals(deviceActor, probe.getLastSender());
    }

    @Test
    void ignoreWrongRegistrationRequest() {
        String trackingId = UUID.randomUUID().toString();

        deviceActor.tell(new RequestTrackDevice(trackingId, "wrongGroup", DEVICE_ID), probe.getRef());
        probe.expectNoMsg();

        trackingId = UUID.randomUUID().toString();

        deviceActor.tell(new RequestTrackDevice(trackingId, "wrongGroup", "wrongDevice"), probe.getRef());
        probe.expectNoMsg();
    }

    @Test
    void ifTemperatureIsNotKnownThenShouldReplyWithEmptyReading() {
        String trackingId = UUID.randomUUID().toString();

        deviceActor.tell(new ReadTemperature(trackingId), probe.getRef());

        RespondTemperature response = probe.expectMsgClass(RespondTemperature.class);

        assertEquals(trackingId, response.getTrackingId());
        assertEquals(Optional.empty(), response.getValue());
    }

    @Test
    void replyWithLatestTemperatureReading() {
        String recordTemperatureTrackingId = UUID.randomUUID().toString();
        String readTemperatureTrackingId = UUID.randomUUID().toString();
        Double value = 17d;

        deviceActor.tell(new RecordTemperature(recordTemperatureTrackingId, value), probe.getRef());
        assertEquals(recordTemperatureTrackingId, probe.expectMsgClass(TemperatureRecorded.class).getTrackingId());

        deviceActor.tell(new ReadTemperature(readTemperatureTrackingId), probe.getRef());

        RespondTemperature response = probe.expectMsgClass(RespondTemperature.class);
        assertEquals(readTemperatureTrackingId, response.getTrackingId());
        assertEquals(Optional.of(value), response.getValue());
    }

    @AfterAll
    static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
}