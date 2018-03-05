package com.kmurawska.playground.akka.iotexample;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.kmurawska.playground.akka.iotexample.devicegroup.message.TrackDeviceRequest;
import com.kmurawska.playground.akka.iotexample.devicemanager.DeviceManager;
import com.kmurawska.playground.akka.iotexample.devicemanager.message.RequestDeviceGroupList;

import java.util.UUID;

public class IoTMain {
    private static final String GROUP_1 = "group1";
    private static final String DEVICE_1 = "device1";
    private static final String DEVICE_2 = "device2";
    private static ActorRef deviceManagerActor;
    private static ActorRef supervisor;

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("iot-system");
        supervisor = system.actorOf(IotSupervisor.props(), "iot-supervisor");

        deviceManagerActor = system.actorOf(DeviceManager.props());

        requestTrackDevice(GROUP_1, DEVICE_1);
        requestTrackDevice(GROUP_1, DEVICE_2);

        deviceManagerActor.tell(new RequestDeviceGroupList(UUID.randomUUID().toString()), supervisor);
    }

    private static void requestTrackDevice(String groupId, String deviceId) {
        deviceManagerActor.tell(new TrackDeviceRequest(UUID.randomUUID().toString(), groupId, deviceId), supervisor);
    }
}