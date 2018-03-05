package com.kmurawska.playground.akka.simple;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.stream.Stream;

public class ClusterApp {

    public static void main(String[] args) {
        if (args.length == 0) {
            start("2551", "2552", "2553");
        } else start(args);
    }

    private static void start(String... ports) {
        Stream.of(ports).forEach(p -> {
            Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + p + "\n" + "akka.remote.artery.canonical.port=" + p).withFallback(ConfigFactory.load());
            ActorSystem actorSystem = ActorSystem.create("ClusterSystem", config);
            actorSystem.actorOf(Props.create(ClusterListener.class), "cluster-listener");
        });
    }
}