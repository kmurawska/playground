package com.kmurawska.playground.akka.transformation;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.kmurawska.playground.akka.transformation.actor.TransformationWorker;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class TransformationWorkerApp {

    public static void main(String[] args) {
        final String port = args.length > 0 ? args[0] : "0";
        final Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port + "\n" + "akka.remote.artery.canonical.port=" + port)
                .withFallback(ConfigFactory.parseString("akka.cluster.roles = [backend]"))
                .withFallback(ConfigFactory.load());

        ActorSystem system = ActorSystem.create("ClusterSystem", config);
        system.actorOf(Props.create(TransformationWorker.class), "transformation-worker");
    }
}