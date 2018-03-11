package com.kmurawska.playground.akka.adaptiveloadbalancing;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import com.kmurawska.playground.akka.adaptiveloadbalancing.actor.FactorialClient;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ClientApp {
    private static final int UP_TO_N = 200;

    public static void main(String[] args) {
        final Config config = ConfigFactory.parseString("akka.cluster.roles = [factorial-client]")
                .withFallback(ConfigFactory.load("adaptive-load-balancing"));

        ActorSystem system = ActorSystem.create("ClusterSystem", config);

        system.log().info("Factorials will start when 2 backend members in the cluster.");

        Cluster.get(system).registerOnMemberUp(() -> {
            system.actorOf(Props.create(FactorialClient.class, UP_TO_N), "factorial");
        });

        Cluster.get(system).registerOnMemberRemoved(() -> {
            system.registerOnTermination(() -> System.exit(0));
            system.terminate();
        });
    }
}