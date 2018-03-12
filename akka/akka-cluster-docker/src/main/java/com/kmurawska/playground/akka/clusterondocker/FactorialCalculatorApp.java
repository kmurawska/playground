package com.kmurawska.playground.akka.clusterondocker;

import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Props;
import akka.cluster.Cluster;
import com.kmurawska.playground.akka.clusterondocker.actor.FactorialCalculator;
import com.kmurawska.playground.akka.clusterondocker.actor.MetricsListener;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static java.lang.System.getenv;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class FactorialCalculatorApp {
    public static void main(String[] args) {
        ActorSystem system = createActorSystem();

        joinSeedNodes(system);

        system.actorOf(Props.create(FactorialCalculator.class), "factorial-calculator");
        system.actorOf(Props.create(MetricsListener.class), "metrics-listener");
    }

    private static ActorSystem createActorSystem() {
        Config config = ConfigFactory.parseString("akka.cluster.roles = [factorial-compute]")
                .withFallback(ConfigFactory.load("router"));

        return ActorSystem.create(System.getenv("cluster_name"), config);
    }

    private static void joinSeedNodes(ActorSystem system) {
        Cluster.get(system).joinSeedNodes(
                stream(getenv().get("seed_nodes").split(","))
                        .map(s -> s.split(":"))
                        .map(s -> new Address("akka", getenv("cluster_name"), s[0], Integer.valueOf(s[1])))
                        .collect(toList())
        );
    }
}