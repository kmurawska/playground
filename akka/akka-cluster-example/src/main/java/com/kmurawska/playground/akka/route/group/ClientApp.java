package com.kmurawska.playground.akka.route.group;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.kmurawska.playground.akka.route.group.actor.SentenceStatisticClient;
import com.kmurawska.playground.akka.route.group.message.CalculateSentenceAverageJob;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public class ClientApp {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("ClusterSystem", ConfigFactory.load("route-group"));
        ActorRef clientActor = system.actorOf(Props.create(SentenceStatisticClient.class, "/user/sentence-statistics-service"), "client");

        final FiniteDuration interval = Duration.create(2, TimeUnit.SECONDS);

        system.scheduler().schedule(interval, interval, () -> clientActor.tell(new CalculateSentenceAverageJob("this is the text"), clientActor), system.dispatcher());
    }
}