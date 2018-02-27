package com.kmurawska.playground.akkaiotexample.devicegroup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.kmurawska.playground.akkaiotexample.device.message.DeviceRegistered;
import com.kmurawska.playground.akkaiotexample.device.message.RecordTemperature;
import com.kmurawska.playground.akkaiotexample.device.message.TemperatureRecorded;
import com.kmurawska.playground.akkaiotexample.devicegroup.message.RequestTrackDevice;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DeviceGroupTest {
    private static final String DEVICE_1_ID = "device1";
    private static final String DEVICE_2_ID = "device2";

    private static final String GROUP = "group";
    private static ActorSystem system;
    private TestKit probe;
    private ActorRef groupActor;

    @BeforeAll
    static void init() {
        system = ActorSystem.create();
    }

    @BeforeEach
    void initTest() {
        probe = new TestKit(system);
        groupActor = system.actorOf(DeviceGroup.props("group"));
    }

    @Test
    void registerDeviceActor() {
        String trackingId = UUID.randomUUID().toString();

        groupActor.tell(new RequestTrackDevice(trackingId, GROUP, DEVICE_1_ID), probe.getRef());
        probe.expectMsgClass(DeviceRegistered.class);
        ActorRef deviceActor1 = probe.getLastSender();

        trackingId = UUID.randomUUID().toString();

        groupActor.tell(new RequestTrackDevice(trackingId, GROUP, DEVICE_2_ID), probe.getRef());
        probe.expectMsgClass(DeviceRegistered.class);
        ActorRef deviceActor2 = probe.getLastSender();

        assertNotEquals(deviceActor1, deviceActor2);

        deviceActor1.tell(new RecordTemperature("device1-trackingId", 1.0), probe.getRef());
        assertEquals("device1-trackingId", probe.expectMsgClass(TemperatureRecorded.class).getTrackingId());
        deviceActor2.tell(new RecordTemperature("device2-trackingId", 2.0), probe.getRef());
        assertEquals("device2-trackingId", probe.expectMsgClass(TemperatureRecorded.class).getTrackingId());

    }

    @Test
    void ignoreRequestForWrongGroupId() {
        String trackingId = UUID.randomUUID().toString();

        groupActor.tell(new RequestTrackDevice(trackingId, "wrongGroup", DEVICE_1_ID), probe.getRef());
        probe.expectNoMsg();
    }

    @Test
    void returnExistingActorForDuplicatedRequest() {
        String trackingId = UUID.randomUUID().toString();

        groupActor.tell(new RequestTrackDevice(trackingId, GROUP, DEVICE_1_ID), probe.getRef());
        probe.expectMsgClass(DeviceRegistered.class);
        ActorRef deviceActor1 = probe.getLastSender();

        groupActor.tell(new RequestTrackDevice(trackingId, GROUP, DEVICE_1_ID), probe.getRef());
        probe.expectMsgClass(DeviceRegistered.class);
        ActorRef deviceActor2 = probe.getLastSender();

        assertEquals(deviceActor1, deviceActor2);
    }

    @AfterAll
    static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
}