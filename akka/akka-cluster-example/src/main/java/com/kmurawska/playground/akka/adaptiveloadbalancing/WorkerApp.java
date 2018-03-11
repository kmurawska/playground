package com.kmurawska.playground.akka.adaptiveloadbalancing;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.kmurawska.playground.akka.adaptiveloadbalancing.actor.FactorialCalculator;
import com.kmurawska.playground.akka.adaptiveloadbalancing.actor.MetricsListener;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.stream.Stream;

public class WorkerApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            start("2551", "2552", "2553");
        } else start(args);
    }

    private static void start(String... ports) {
        Stream.of(ports).forEach(p -> {
            Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + p + "\n" + "akka.remote.artery.canonical.port=" + p)
                    .withFallback(ConfigFactory.parseString("akka.cluster.roles = [factorial-compute]"))
                    .withFallback(ConfigFactory.load("adaptive-load-balancing"));

            ActorSystem system = ActorSystem.create("ClusterSystem", config);
            system.actorOf(Props.create(FactorialCalculator.class), "factorial-calculator");
            system.actorOf(Props.create(MetricsListener.class), "metrics-listener");
        });
    }
}