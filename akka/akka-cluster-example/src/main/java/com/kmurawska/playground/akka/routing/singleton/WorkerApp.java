package com.kmurawska.playground.akka.routing.singleton;

import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.cluster.singleton.ClusterSingletonManager;
import akka.cluster.singleton.ClusterSingletonManagerSettings;
import akka.cluster.singleton.ClusterSingletonProxy;
import akka.cluster.singleton.ClusterSingletonProxySettings;
import com.kmurawska.playground.akka.routing.sentence.actor.SentenceStatisticService;
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
                    .withFallback(ConfigFactory.parseString("akka.cluster.roles = [compute]"))
                    .withFallback(ConfigFactory.load("route-pool-singleton"));

            ActorSystem system = ActorSystem.create("ClusterSystem", config);

            ClusterSingletonManagerSettings settings = ClusterSingletonManagerSettings.create(system).withRole("compute");
            system.actorOf(ClusterSingletonManager.props(Props.create(SentenceStatisticService.class), PoisonPill.getInstance(), settings), "sentence-statistics-service");

            ClusterSingletonProxySettings proxySettings = ClusterSingletonProxySettings.create(system).withRole("compute");
            system.actorOf(ClusterSingletonProxy.props("/user/sentence-statistics-service", proxySettings), "sentence-statistics-service-proxy");
        });
    }
}