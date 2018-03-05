package com.kmurawska.playground.akka.iotexample.devicegroup;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.testkit.javadsl.TestKit;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.message.AllTemperaturesRequest;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.message.ReplyAllTemperatures;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.temperature.DeviceTemperature;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.temperature.DeviceTemperatureNotAvailable;
import com.kmurawska.playground.akka.iotexample.device.message.DeviceRegistered;
import com.kmurawska.playground.akka.iotexample.device.message.RecordTemperature;
import com.kmurawska.playground.akka.iotexample.device.message.TemperatureRecorded;
import com.kmurawska.playground.akka.iotexample.devicegroup.message.DeviceListRequest;
import com.kmurawska.playground.akka.iotexample.devicegroup.message.ResponseDeviceList;
import com.kmurawska.playground.akka.iotexample.devicegroup.message.TrackDeviceRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.*;

class DeviceGroupTest {
    private static final String GROUP = "group";
    private static final String DEVICE_ID_1 = "device1";
    private static final String DEVICE_ID_2 = "device2";
    private static final String DEVICE_ID_3 = "device3";
    private static final String TRACKING_ID_1 = "tracking-id-1";
    private static final String TRACKING_ID_2 = "tracking-id-2";
    private static final double TEMPERATURE_DEVICE_1 = 1.0;
    private static final double TEMPERATURE_DEVICE_2 = 2.0;

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
        groupActor.tell(new TrackDeviceRequest(TRACKING_ID_1, "wrongGroup", DEVICE_ID_1), probe.getRef());

        probe.expectNoMsg();
    }

    @Test
    void requestTrackDevicesForOneGroupShouldResultInTwoDifferentDeviceActors() {
        groupActor.tell(new TrackDeviceRequest(TRACKING_ID_1, GROUP, DEVICE_ID_1), probe.getRef());
        assertEquals(TRACKING_ID_1, probe.expectMsgClass(DeviceRegistered.class).getTrackingId());
        ActorRef deviceActor1 = probe.getLastSender();

        groupActor.tell(new TrackDeviceRequest(TRACKING_ID_2, GROUP, DEVICE_ID_2), probe.getRef());
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
        groupActor.tell(new TrackDeviceRequest(TRACKING_ID_1, GROUP, DEVICE_ID_1), probe.getRef());
        assertEquals(TRACKING_ID_1, probe.expectMsgClass(DeviceRegistered.class).getTrackingId());
        ActorRef deviceActor1 = probe.getLastSender();

        groupActor.tell(new TrackDeviceRequest(TRACKING_ID_2, GROUP, DEVICE_ID_1), probe.getRef());
        assertEquals(TRACKING_ID_2, probe.expectMsgClass(DeviceRegistered.class).getTrackingId());
        ActorRef deviceActor2 = probe.getLastSender();

        assertEquals(deviceActor1, deviceActor2);
    }

    @Test
    void listActiveDevices() {
        requestTrackDevice(GROUP, DEVICE_ID_1);
        requestTrackDevice(GROUP, DEVICE_ID_2);

        groupActor.tell(new DeviceListRequest(TRACKING_ID_1), probe.getRef());
        ResponseDeviceList replyDeviceList = probe.expectMsgClass(ResponseDeviceList.class);

        assertEquals(TRACKING_ID_1, replyDeviceList.getTrackingId());
        assertEquals(of(DEVICE_ID_1, DEVICE_ID_2).collect(toSet()), replyDeviceList.getDeviceIds());
    }

    @Test
    void listActiveDevicesWhenOneShutsDown() {
        groupActor.tell(new TrackDeviceRequest(TRACKING_ID_1, GROUP, DEVICE_ID_1), probe.getRef());
        probe.expectMsgClass(DeviceRegistered.class);

        ActorRef actorToShutdown = probe.getLastSender();
        requestTrackDevice(GROUP, DEVICE_ID_2);

        groupActor.tell(new DeviceListRequest(TRACKING_ID_1), probe.getRef());
        ResponseDeviceList replyDeviceList = probe.expectMsgClass(ResponseDeviceList.class);

        assertEquals(TRACKING_ID_1, replyDeviceList.getTrackingId());
        assertEquals(of(DEVICE_ID_1, DEVICE_ID_2).collect(toSet()), replyDeviceList.getDeviceIds());

        probe.watch(actorToShutdown);
        actorToShutdown.tell(PoisonPill.getInstance(), ActorRef.noSender());
        probe.expectTerminated(actorToShutdown);

        probe.awaitAssert(() -> {
            groupActor.tell(new DeviceListRequest(TRACKING_ID_2), probe.getRef());
            ResponseDeviceList r = probe.expectMsgClass(ResponseDeviceList.class);
            assertEquals(TRACKING_ID_2, r.getTrackingId());
            assertEquals(of(DEVICE_ID_2).collect(toSet()), r.getDeviceIds());
            return null;
        });
    }

    @Test
    void collectTemperaturesFromAllDevices() {
        ActorRef deviceActor1 = requestTrackDevice(GROUP, DEVICE_ID_1);
        ActorRef deviceActor2 = requestTrackDevice(GROUP, DEVICE_ID_2);
        requestTrackDevice(GROUP, DEVICE_ID_3);

        deviceActor1.tell(new RecordTemperature(TRACKING_ID_1, TEMPERATURE_DEVICE_1), probe.getRef());
        probe.expectMsgClass(TemperatureRecorded.class);
        deviceActor2.tell(new RecordTemperature(TRACKING_ID_1, TEMPERATURE_DEVICE_2), probe.getRef());
        probe.expectMsgClass(TemperatureRecorded.class);

        groupActor.tell(new AllTemperaturesRequest(TRACKING_ID_1), probe.getRef());

        ReplyAllTemperatures response = probe.expectMsgClass(ReplyAllTemperatures.class);

        assertEquals(TRACKING_ID_1, response.getTrackingId());
        assertEquals(3, response.getTemperatures().size());
        assertTrue(response.getTemperatures().get(DEVICE_ID_1) instanceof DeviceTemperature);
        assertEquals(TEMPERATURE_DEVICE_1, ((DeviceTemperature) response.getTemperatures().get(DEVICE_ID_1)).getValue().doubleValue());
        assertTrue(response.getTemperatures().get(DEVICE_ID_2) instanceof DeviceTemperature);
        assertEquals(TEMPERATURE_DEVICE_2, ((DeviceTemperature) response.getTemperatures().get(DEVICE_ID_2)).getValue().doubleValue());
        assertTrue(response.getTemperatures().get(DEVICE_ID_3) instanceof DeviceTemperatureNotAvailable);
    }

    private ActorRef requestTrackDevice(String groupId, String deviceId) {
        groupActor.tell(new TrackDeviceRequest(TRACKING_ID_1, groupId, deviceId), probe.getRef());
        probe.expectMsgClass(DeviceRegistered.class);
        return probe.getLastSender();
    }

    @AfterAll
    static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
}