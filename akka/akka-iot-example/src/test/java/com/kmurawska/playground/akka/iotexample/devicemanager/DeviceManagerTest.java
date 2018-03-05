package com.kmurawska.playground.akka.iotexample.devicemanager;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.testkit.javadsl.TestKit;
import com.kmurawska.playground.akka.iotexample.devicemanager.message.ReplyDeviceGroupList;
import com.kmurawska.playground.akka.iotexample.devicemanager.message.RequestDeviceGroupList;
import com.kmurawska.playground.akka.iotexample.device.message.DeviceRegistered;
import com.kmurawska.playground.akka.iotexample.devicegroup.message.TrackDeviceRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceManagerTest {
    private static final String GROUP_1 = "group1";
    private static final String GROUP_2 = "group2";
    private static final String DEVICE_ID_1 = "device1";
    private static final String DEVICE_ID_2 = "device2";
    private static final String TRACKING_ID_1 = "tracking-id-1";

    private static ActorSystem system;
    private TestKit probe;
    private ActorRef deviceManagerActor;

    @BeforeAll
    static void init() {
        system = ActorSystem.create();
    }

    @BeforeEach
    void initTest() {
        probe = new TestKit(system);
        deviceManagerActor = system.actorOf(DeviceManager.props());
    }

    @Test
    void requestTrackDeviceForOneGroupShouldResultInOneActiveDeviceGroup() {
        requestTrackDevice(GROUP_1, DEVICE_ID_1);
        requestTrackDevice(GROUP_1, DEVICE_ID_2);

        deviceManagerActor.tell(new RequestDeviceGroupList(TRACKING_ID_1), probe.getRef());
        ReplyDeviceGroupList replyDeviceGroupList = probe.expectMsgClass(ReplyDeviceGroupList.class);

        assertEquals(TRACKING_ID_1, replyDeviceGroupList.getTrackingId());
        assertEquals(Stream.of(GROUP_1).collect(toSet()), replyDeviceGroupList.getDeviceGroups());
    }

    @Test
    void requestTrackDeviceForTwoDifferentGroupsShouldResultInTwoActiveDeviceGroups() {
        requestTrackDevice(GROUP_1, DEVICE_ID_1);
        requestTrackDevice(GROUP_2, DEVICE_ID_2);

        deviceManagerActor.tell(new RequestDeviceGroupList(TRACKING_ID_1), probe.getRef());
        ReplyDeviceGroupList replyDeviceGroupList = probe.expectMsgClass(ReplyDeviceGroupList.class);

        assertEquals(TRACKING_ID_1, replyDeviceGroupList.getTrackingId());
        assertEquals(Stream.of(GROUP_1, GROUP_2).collect(toSet()), replyDeviceGroupList.getDeviceGroups());
    }

    @Test
    void requestTrackDeviceForTwoDifferentGroupsShouldResultInOneActiveDeviceGroupWhenOneWentDown() {
        requestTrackDevice(GROUP_1, DEVICE_ID_1);
        requestTrackDevice(GROUP_2, DEVICE_ID_2);

        system.actorSelection(deviceManagerActor.path().child("group-" + GROUP_1)).tell(PoisonPill.getInstance(), ActorRef.noSender());

        probe.awaitAssert(() -> {
            deviceManagerActor.tell(new RequestDeviceGroupList(TRACKING_ID_1), probe.getRef());
            ReplyDeviceGroupList r = probe.expectMsgClass(ReplyDeviceGroupList.class);
            assertEquals(TRACKING_ID_1, r.getTrackingId());
            assertEquals(Stream.of(GROUP_2).collect(toSet()), r.getDeviceGroups());
            return null;
        });
    }

    private void requestTrackDevice(String groupId, String deviceId) {
        deviceManagerActor.tell(new TrackDeviceRequest(TRACKING_ID_1, groupId, deviceId), probe.getRef());
        assertEquals(TRACKING_ID_1, probe.expectMsgClass(DeviceRegistered.class).getTrackingId());
    }

    @AfterAll
    static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
}