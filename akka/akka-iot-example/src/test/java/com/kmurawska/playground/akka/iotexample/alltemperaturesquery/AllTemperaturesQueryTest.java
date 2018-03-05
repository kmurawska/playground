package com.kmurawska.playground.akka.iotexample.alltemperaturesquery;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.testkit.javadsl.TestKit;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.message.ReplyAllTemperatures;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.temperature.DeviceTimedOut;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.temperature.DeviceTemperature;
import com.kmurawska.playground.akka.iotexample.alltemperaturesquery.temperature.DeviceTemperatureNotAvailable;
import com.kmurawska.playground.akka.iotexample.device.DeviceActor;
import com.kmurawska.playground.akka.iotexample.device.message.DeviceTemperatureResponse;
import com.kmurawska.playground.akka.iotexample.device.message.ReadTemperature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import scala.concurrent.duration.FiniteDuration;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AllTemperaturesQueryTest {
    private static final String TRACKING_ID_1 = "tracking-id-1";
    private static final String TRACKING_ID_2 = "tracking-id-2";
    private static final String DEVICE_1 = "device1";
    private static final String DEVICE_2 = "device2";
    private static final double TEMPERATURE_DEVICE_1 = 1.0;
    private static final double TEMPERATURE_DEVICE_2 = 2.0;

    private static ActorSystem system;
    private TestKit requester;
    private TestKit device1;
    private TestKit device2;

    private Set<DeviceActor> devices = new HashSet<>();

    @BeforeAll
    static void init() {
        system = ActorSystem.create();
    }

    @BeforeEach
    void initTest() {
        this.requester = new TestKit(system);
        this.device1 = new TestKit(system);
        this.device2 = new TestKit(system);

        devices.add(new DeviceActor(DEVICE_1, device1.getRef()));
        devices.add(new DeviceActor(DEVICE_2, device2.getRef()));
    }

    @Test
    void creationOfActorShouldResultInSendingMessagesToDevices() {
        system.actorOf(AllTemperaturesQuery.props(devices, TRACKING_ID_1, requester.getRef(), new FiniteDuration(3, TimeUnit.SECONDS)));

        assertEquals(TRACKING_ID_1, device1.expectMsgClass(ReadTemperature.class).getTrackingId());
        assertEquals(TRACKING_ID_1, device2.expectMsgClass(ReadTemperature.class).getTrackingId());
    }

    @Test
    void whenAllDevicesAreWorkingQueryShouldAnswerWithTemperatureValue() {
        ActorRef queryActor = system.actorOf(AllTemperaturesQuery.props(devices, TRACKING_ID_1, requester.getRef(), new FiniteDuration(3, TimeUnit.SECONDS)));

        assertEquals(TRACKING_ID_1, device1.expectMsgClass(ReadTemperature.class).getTrackingId());
        assertEquals(TRACKING_ID_1, device2.expectMsgClass(ReadTemperature.class).getTrackingId());

        queryActor.tell(new DeviceTemperatureResponse(TRACKING_ID_2, Optional.of(TEMPERATURE_DEVICE_1)), device1.getRef());
        queryActor.tell(new DeviceTemperatureResponse(TRACKING_ID_2, Optional.of(TEMPERATURE_DEVICE_2)), device2.getRef());

        ReplyAllTemperatures response = requester.expectMsgClass(ReplyAllTemperatures.class);

        assertEquals(TRACKING_ID_1, response.getTrackingId());
        assertEquals(2, response.getTemperatures().size());
        assertEquals(TEMPERATURE_DEVICE_1, ((DeviceTemperature) response.getTemperatures().get(DEVICE_1)).getValue().doubleValue());
        assertEquals(TEMPERATURE_DEVICE_2, ((DeviceTemperature) response.getTemperatures().get(DEVICE_2)).getValue().doubleValue());
    }

    @Test
    void whenOneDeviceHasNoTemperatureQueryShouldAnswerWithTemperatureNotAvailable() {
        ActorRef queryActor = system.actorOf(AllTemperaturesQuery.props(devices, TRACKING_ID_1, requester.getRef(), new FiniteDuration(3, TimeUnit.SECONDS)));

        assertEquals(TRACKING_ID_1, device1.expectMsgClass(ReadTemperature.class).getTrackingId());
        assertEquals(TRACKING_ID_1, device2.expectMsgClass(ReadTemperature.class).getTrackingId());

        queryActor.tell(new DeviceTemperatureResponse(TRACKING_ID_2, Optional.empty()), device1.getRef());
        queryActor.tell(new DeviceTemperatureResponse(TRACKING_ID_2, Optional.of(TEMPERATURE_DEVICE_2)), device2.getRef());

        ReplyAllTemperatures response = requester.expectMsgClass(ReplyAllTemperatures.class);
        assertEquals(TRACKING_ID_1, response.getTrackingId());
        assertEquals(2, response.getTemperatures().size());
        assertTrue(response.getTemperatures().get(DEVICE_1) instanceof DeviceTemperatureNotAvailable);
        assertEquals(TEMPERATURE_DEVICE_2, ((DeviceTemperature) response.getTemperatures().get(DEVICE_2)).getValue().doubleValue());
    }

    @Test
    void whenOneDeviceStoppedBeforeAnsweringQueryShouldAnswerWithTemperatureNotAvailable() {
        ActorRef queryActor = system.actorOf(AllTemperaturesQuery.props(devices, TRACKING_ID_1, requester.getRef(), new FiniteDuration(3, TimeUnit.SECONDS)));

        assertEquals(TRACKING_ID_1, device1.expectMsgClass(ReadTemperature.class).getTrackingId());
        assertEquals(TRACKING_ID_1, device2.expectMsgClass(ReadTemperature.class).getTrackingId());

        device1.getRef().tell(PoisonPill.getInstance(), ActorRef.noSender());
        queryActor.tell(new DeviceTemperatureResponse(TRACKING_ID_2, Optional.of(TEMPERATURE_DEVICE_2)), device2.getRef());

        ReplyAllTemperatures response = requester.expectMsgClass(ReplyAllTemperatures.class);
        assertEquals(TRACKING_ID_1, response.getTrackingId());
        assertEquals(2, response.getTemperatures().size());
        //    assertEquals(DEVICE_NOT_AVAILABLE, response.getTemperatures().get(DEVICE_1).getStatus());
        assertEquals(TEMPERATURE_DEVICE_2, ((DeviceTemperature) response.getTemperatures().get(DEVICE_2)).getValue().doubleValue());
    }

    @Test
    void whenOneDeviceStoppedAfterAnsweringQueryShouldAnswerWithTemperatureValue() {
        ActorRef queryActor = system.actorOf(AllTemperaturesQuery.props(devices, TRACKING_ID_1, requester.getRef(), new FiniteDuration(3, TimeUnit.SECONDS)));

        assertEquals(TRACKING_ID_1, device1.expectMsgClass(ReadTemperature.class).getTrackingId());
        assertEquals(TRACKING_ID_1, device2.expectMsgClass(ReadTemperature.class).getTrackingId());

        queryActor.tell(new DeviceTemperatureResponse(TRACKING_ID_2, Optional.of(TEMPERATURE_DEVICE_1)), device1.getRef());
        queryActor.tell(new DeviceTemperatureResponse(TRACKING_ID_2, Optional.of(TEMPERATURE_DEVICE_2)), device2.getRef());

        device1.getRef().tell(PoisonPill.getInstance(), ActorRef.noSender());

        ReplyAllTemperatures response = requester.expectMsgClass(ReplyAllTemperatures.class);
        assertEquals(TRACKING_ID_1, response.getTrackingId());
        assertEquals(2, response.getTemperatures().size());
        assertEquals(TEMPERATURE_DEVICE_1, ((DeviceTemperature) response.getTemperatures().get(DEVICE_1)).getValue().doubleValue());
        assertEquals(TEMPERATURE_DEVICE_2, ((DeviceTemperature) response.getTemperatures().get(DEVICE_2)).getValue().doubleValue());
    }

    @Test
    void whenDeviceHasTimeoutQueryShouldAnswerWithDeviceTimeout() {
        ActorRef queryActor = system.actorOf(AllTemperaturesQuery.props(devices, TRACKING_ID_1, requester.getRef(), new FiniteDuration(3, TimeUnit.SECONDS)));

        assertEquals(TRACKING_ID_1, device1.expectMsgClass(ReadTemperature.class).getTrackingId());
        assertEquals(TRACKING_ID_1, device2.expectMsgClass(ReadTemperature.class).getTrackingId());

        queryActor.tell(new DeviceTemperatureResponse(TRACKING_ID_2, Optional.of(TEMPERATURE_DEVICE_2)), device2.getRef());

        ReplyAllTemperatures response = requester.expectMsgClass(FiniteDuration.create(5, TimeUnit.SECONDS), ReplyAllTemperatures.class);
        assertEquals(TRACKING_ID_1, response.getTrackingId());
        assertEquals(2, response.getTemperatures().size());
        assertTrue(response.getTemperatures().get(DEVICE_1) instanceof DeviceTimedOut);
        assertEquals(TEMPERATURE_DEVICE_2, ((DeviceTemperature) response.getTemperatures().get(DEVICE_2)).getValue().doubleValue());
    }

    @AfterAll
    static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
}