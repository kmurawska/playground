package com.kmurawska.playground.akka.iotexample.device;

import akka.actor.ActorRef;

public class DeviceActor {
    private String deviceId;
    private ActorRef actorRef;

    public DeviceActor() {
    }

    public DeviceActor(String deviceId, ActorRef actorRef) {
        this.deviceId = deviceId;
        this.actorRef = actorRef;
    }

    public String getDeviceId() {
        return deviceId;
    }


    public ActorRef getActorRef() {
        return actorRef;
    }
}
