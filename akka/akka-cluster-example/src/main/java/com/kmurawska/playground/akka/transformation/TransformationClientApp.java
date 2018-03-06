package com.kmurawska.playground.akka.transformation;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.kmurawska.playground.akka.transformation.actor.TransformationClient;
import com.kmurawska.playground.akka.transformation.message.TransformationJob;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

import static com.typesafe.config.ConfigFactory.parseString;
import static java.util.UUID.randomUUID;

public class TransformationClientApp {
    public static void main(String[] args) {
        final String port = args.length > 0 ? args[0] : "0";

        final Config config = parseString("akka.remote.netty.tcp.port=" + port + "\n" + "akka.remote.artery.canonical.port=" + port)
                .withFallback(parseString("akka.cluster.roles = [client]"))
                .withFallback(ConfigFactory.load());

        ActorSystem system = ActorSystem.create("ClusterSystem", config);

        final ActorRef transformationClient = system.actorOf(Props.create(TransformationClient.class), "transformation-client");
        final FiniteDuration interval = Duration.create(2, TimeUnit.SECONDS);

        system.scheduler().schedule(interval, interval, () -> transformationClient.tell(new TransformationJob(randomUUID().toString(), "Text-" + randomUUID().toString()), ActorRef.noSender()), system.dispatcher());
    }
}