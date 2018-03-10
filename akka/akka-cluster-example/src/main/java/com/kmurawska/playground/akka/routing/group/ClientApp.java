package com.kmurawska.playground.akka.routing.group;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.kmurawska.playground.akka.routing.sentence.SentenceGenerator;
import com.kmurawska.playground.akka.routing.sentence.actor.ClusterListener;
import com.kmurawska.playground.akka.routing.sentence.message.CalculateSentenceAverageJob;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ClientApp {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("ClusterSystem", ConfigFactory.load("route-group"));
        ActorRef clusterListener = system.actorOf(Props.create(ClusterListener.class, "/user/sentence-statistics-service"), "client");

        final FiniteDuration interval = Duration.create(2, TimeUnit.SECONDS);

        SentenceGenerator sentenceGenerator = new SentenceGenerator();
        system.scheduler().schedule(interval, interval, () -> clusterListener.tell(new CalculateSentenceAverageJob(UUID.randomUUID().toString(), sentenceGenerator.generate()), clusterListener), system.dispatcher());
    }

}