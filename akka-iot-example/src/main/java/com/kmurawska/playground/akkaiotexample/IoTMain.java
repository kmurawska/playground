package com.kmurawska.playground.akkaiotexample;

import akka.actor.ActorSystem;

public class IoTMain {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("iot-system");
        system.actorOf(IotSupervisor.props(), "iot-supervisior");
    }
}