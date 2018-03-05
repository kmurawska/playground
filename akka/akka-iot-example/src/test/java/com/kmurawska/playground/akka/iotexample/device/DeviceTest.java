package com.kmurawska.playground.akka.iotexample.device;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.kmurawska.playground.akka.iotexample.device.message.*;
import com.kmurawska.playground.akka.iotexample.devicegroup.message.TrackDeviceRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceTest {
    private static final String GROUP_ID = "group";
    private static final String DEVICE_ID = "device";
    private static final String TRACKING_ID_1 = "tracking-id-1";
    private static final String TRACKING_ID_2 = "tracking-id-2";
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
        deviceActor.tell(new TrackDeviceRequest(TRACKING_ID_1, GROUP_ID, DEVICE_ID), probe.getRef());

        assertEquals(TRACKING_ID_1, probe.expectMsgClass(DeviceRegistered.class).getTrackingId());
        assertEquals(deviceActor, probe.getLastSender());
    }

    @Test
    void ignoreWrongRegistrationRequest() {
        deviceActor.tell(new TrackDeviceRequest(TRACKING_ID_1, "wrongGroup", DEVICE_ID), probe.getRef());
        probe.expectNoMsg();


        deviceActor.tell(new TrackDeviceRequest(TRACKING_ID_2, "wrongGroup", "wrongDevice"), probe.getRef());
        probe.expectNoMsg();
    }

    @Test
    void ifTemperatureIsNotKnownThenShouldReplyWithEmptyReading() {
        deviceActor.tell(new ReadTemperature(TRACKING_ID_1), probe.getRef());

        DeviceTemperatureResponse response = probe.expectMsgClass(DeviceTemperatureResponse.class);

        assertEquals(TRACKING_ID_1, response.getTrackingId());
        assertEquals(Optional.empty(), response.getValue());
    }

    @Test
    void replyWithLatestTemperatureReading() {
        Double value = 17d;

        deviceActor.tell(new RecordTemperature(TRACKING_ID_1, value), probe.getRef());
        assertEquals(TRACKING_ID_1, probe.expectMsgClass(TemperatureRecorded.class).getTrackingId());

        deviceActor.tell(new ReadTemperature(TRACKING_ID_2), probe.getRef());

        DeviceTemperatureResponse response = probe.expectMsgClass(DeviceTemperatureResponse.class);
        assertEquals(TRACKING_ID_2, response.getTrackingId());
        assertEquals(Optional.of(value), response.getValue());
    }

    @AfterAll
    static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
}