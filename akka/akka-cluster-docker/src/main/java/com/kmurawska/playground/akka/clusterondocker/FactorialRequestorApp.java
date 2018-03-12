package com.kmurawska.playground.akka.clusterondocker;

import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Props;
import akka.cluster.Cluster;
import com.kmurawska.playground.akka.clusterondocker.actor.FactorialRequestor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import static java.lang.System.getenv;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class FactorialRequestorApp {
    private static final int UP_TO_N = 200;

    public static void main(String[] args) {
        ActorSystem system = createActorSystem();

        system.log().info("Factorials will start when 2 backend members in the cluster.");

        joinSeedNodes(system);

        Cluster.get(system).registerOnMemberUp(() -> system.actorOf(Props.create(FactorialRequestor.class, UP_TO_N), "factorial-requestor"));
        Cluster.get(system).registerOnMemberRemoved(() -> {
            system.registerOnTermination(() -> System.exit(0));
            system.terminate();
        });
    }

    private static ActorSystem createActorSystem() {
        final Config config = ConfigFactory.parseString("akka.cluster.roles = [factorial-client]")
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