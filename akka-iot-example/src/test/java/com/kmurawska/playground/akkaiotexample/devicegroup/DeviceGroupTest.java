package com.kmurawska.playground.akkaiotexample.devicegroup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.testkit.javadsl.TestKit;
import com.kmurawska.playground.akkaiotexample.device.message.DeviceRegistered;
import com.kmurawska.playground.akkaiotexample.device.message.RecordTemperature;
import com.kmurawska.playground.akkaiotexample.device.message.TemperatureRecorded;
import com.kmurawska.playground.akkaiotexample.devicegroup.message.ReplyDeviceList;
import com.kmurawska.playground.akkaiotexample.devicegroup.message.RequestDeviceList;
import com.kmurawska.playground.akkaiotexample.devicegroup.message.RequestTrackDevice;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DeviceGroupTest {
    private static final String GROUP = "group";
    private static final String DEVICE_ID_1 = "device1";
    private static final String DEVICE_ID_2 = "device2";
    private static final String TRACKING_ID_1 = "tracking-id-1";
    private static final String TRACKING_ID_2 = "tracking-id-2";

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
    void ignoreRequestForWrongGroupId() {
        groupActor.tell(new RequestTrackDevice(TRACKING_ID_1, "wrongGroup", DEVICE_ID_1), probe.getRef());

        probe.expectNoMsg();
    }

    @Test
    void requestTrackDevicesForOneGroupShouldResultInTwoDifferentDeviceActors() {
        groupActor.tell(new RequestTrackDevice(TRACKING_ID_1, GROUP, DEVICE_ID_1), probe.getRef());
        assertEquals(TRACKING_ID_1, probe.expectMsgClass(DeviceRegistered.class).getTrackingId());
        ActorRef deviceActor1 = probe.getLastSender();

        groupActor.tell(new RequestTrackDevice(TRACKING_ID_2, GROUP, DEVICE_ID_2), probe.getRef());
        assertEquals(TRACKING_ID_2, probe.expectMsgClass(DeviceRegistered.class).getTrackingId());
        ActorRef deviceActor2 = probe.getLastSender();

        assertNotEquals(deviceActor1, deviceActor2);

        deviceActor1.tell(new RecordTemperature("device1-trackingId", 1.0), probe.getRef());
        assertEquals("device1-trackingId", probe.expectMsgClass(TemperatureRecorded.class).getTrackingId());

        deviceActor2.tell(new RecordTemperature("device2-trackingId", 2.0), probe.getRef());
        assertEquals("device2-trackingId", probe.expectMsgClass(TemperatureRecorded.class).getTrackingId());
    }

    @Test
    void requestOneTrackDeviceTwoTimesShouldResultInOneDeviceActor() {
        groupActor.tell(new RequestTrackDevice(TRACKING_ID_1, GROUP, DEVICE_ID_1), probe.getRef());
        assertEquals(TRACKING_ID_1, probe.expectMsgClass(DeviceRegistered.class).getTrackingId());
        ActorRef deviceActor1 = probe.getLastSender();

        groupActor.tell(new RequestTrackDevice(TRACKING_ID_2, GROUP, DEVICE_ID_1), probe.getRef());
        assertEquals(TRACKING_ID_2, probe.expectMsgClass(DeviceRegistered.class).getTrackingId());
        ActorRef deviceActor2 = probe.getLastSender();

        assertEquals(deviceActor1, deviceActor2);
    }

    @Test
    void listActiveDevices() {
        requestTrackDevice(GROUP, DEVICE_ID_1);
        requestTrackDevice(GROUP, DEVICE_ID_2);

        groupActor.tell(new RequestDeviceList(TRACKING_ID_1), probe.getRef());
        ReplyDeviceList replyDeviceList = probe.expectMsgClass(ReplyDeviceList.class);

        assertEquals(TRACKING_ID_1, replyDeviceList.getTrackingId());
        assertEquals(Stream.of(DEVICE_ID_1, DEVICE_ID_2).collect(toSet()), replyDeviceList.getDevices());
    }

    @Test
    void listActiveDevicesWhenOneShutsDown() {
        groupActor.tell(new RequestTrackDevice(TRACKING_ID_1, GROUP, DEVICE_ID_1), probe.getRef());
        probe.expectMsgClass(DeviceRegistered.class);

        ActorRef actorToShutdown = probe.getLastSender();
        requestTrackDevice(GROUP, DEVICE_ID_2);

        groupActor.tell(new RequestDeviceList(TRACKING_ID_1), probe.getRef());
        ReplyDeviceList replyDeviceList = probe.expectMsgClass(ReplyDeviceList.class);

        assertEquals(TRACKING_ID_1, replyDeviceList.getTrackingId());
        assertEquals(Stream.of(DEVICE_ID_1, DEVICE_ID_2).collect(toSet()), replyDeviceList.getDevices());

        probe.watch(actorToShutdown);
        actorToShutdown.tell(PoisonPill.getInstance(), ActorRef.noSender());
        probe.expectTerminated(actorToShutdown);

        probe.awaitAssert(() -> {
            groupActor.tell(new RequestDeviceList(TRACKING_ID_2), probe.getRef());
            ReplyDeviceList r = probe.expectMsgClass(ReplyDeviceList.class);
            assertEquals(TRACKING_ID_2, r.getTrackingId());
            assertEquals(Stream.of(DEVICE_ID_2).collect(toSet()), r.getDevices());
            return null;
        });
    }

    private void requestTrackDevice(String groupId, String deviceId) {
        groupActor.tell(new RequestTrackDevice(TRACKING_ID_1, groupId, deviceId), probe.getRef());
        probe.expectMsgClass(DeviceRegistered.class);
    }

    @AfterAll
    static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
}