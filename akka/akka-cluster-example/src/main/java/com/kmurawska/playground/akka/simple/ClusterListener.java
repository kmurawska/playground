package com.kmurawska.playground.akka.simple;

import akka.actor.AbstractActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ClusterListener extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private Cluster cluster = Cluster.get(getContext().system());

    @Override
    public void preStart() {
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                .match(ClusterEvent.MemberUp.class, m -> log.info("Member {} is up.", m.member()))
                .match(ClusterEvent.UnreachableMember.class, m -> log.info("Member {} is unreachable.", m.member()))
                .match(ClusterEvent.MemberRemoved.class, m -> log.info("Member {} is removed", m.member()))
                .build();
    }

    @Override
    public void postStop() {
        cluster.unsubscribe(getSelf());
    }
}