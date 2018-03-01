package com.kmurawska.playground.akkaiotexample.devicegroupquery;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.kmurawska.playground.akkaiotexample.device.message.RespondTemperature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scala.concurrent.duration.FiniteDuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceGroupQueryTest {
    private static final String TRACKING_ID_1 = "tracking-id-1";
    private static final String TRACKING_ID_2 = "tracking-id-1";

    private static ActorSystem system;
    private TestKit requester;
    private TestKit device1;
    private TestKit device2;

    @BeforeAll
    static void init() {
        system = ActorSystem.create();
    }

    @BeforeEach
    void initTest() {
        this.requester = new TestKit(system);
        this.device1 = new TestKit(system);
        this.device2 = new TestKit(system);
    }

    @Test
    void returnTemperatureValueForWorkingDevices() {
        Map<ActorRef, String> devices = new HashMap<>();
        devices.put(device1.getRef(), "device1");
        devices.put(device2.getRef(), "device2");

        ActorRef queryActor = system.actorOf(DeviceGroupQuery.props(devices, TRACKING_ID_1, requester.getRef(), new FiniteDuration(3, TimeUnit.SECONDS)));

        assertEquals(TRACKING_ID_1, device1.expectMsgClass(RespondTemperature.class).getTrackingId());
        assertEquals(TRACKING_ID_1, device2.expectMsgClass(RespondTemperature.class).getTrackingId());

        queryActor.tell(new RespondTemperature(TRACKING_ID_2, Optional.of(1.0)), device1.getRef());
        queryActor.tell(new RespondTemperature(TRACKING_ID_2, Optional.of(2.0)), device2.getRef());

        ReplyAllTemperatures response = requester.expectMsgClass(ReplyAllTemperatures.class);
        assertEquals("A", response.getTrackingId());

        Map<String, TemperatureReading> expectedTemperatures = new HashMap<>();
        expectedTemperatures.put("device1", new Temperature(1.0));
        expectedTemperatures.put("device2", new Temperature(2.0));

        assertEquals(expectedTemperatures, response.getTemperatures());
    }

    @AfterAll
    static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
}