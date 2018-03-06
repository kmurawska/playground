package com.kmurawska.playground.akka.route.group.actor;

import akka.actor.AbstractActor;
import akka.actor.Address;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.*;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akka.route.group.message.CalculateSentenceAverageJob;
import com.kmurawska.playground.akka.route.group.message.SentenceAverageResult;
import com.kmurawska.playground.akka.route.group.message.SentenceStatisticFailed;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class SentenceStatisticClient extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final String servicePath;
    private final Set<Address> nodes = new HashSet<>();
    private Cluster cluster = Cluster.get(getContext().system());

    public SentenceStatisticClient(String servicePath) {
        this.servicePath = servicePath;
    }

    @Override
    public void preStart() {
        cluster.subscribe(self(), MemberEvent.class, ClusterEvent.ReachabilityEvent.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CalculateSentenceAverageJob.class, this::onSentenceStatisticJob)
                .match(SentenceAverageResult.class, this::onSentenceStatisticResult)
                .match(SentenceStatisticFailed.class, this::onSentenceStatisticFailed)
                .match(CurrentClusterState.class, this::onCurrentClusterState)
                .match(MemberUp.class, this::onMemberUp)
                .match(ReachableMember.class, this::onReachableMember)
                .match(MemberEvent.class, this::onMemberEvent)
                .match(UnreachableMember.class, this::onUnreachableMember)
                .build();
    }

    private void onSentenceStatisticJob(CalculateSentenceAverageJob job) {
        Address address = new ArrayList<>(nodes).get(ThreadLocalRandom.current().nextInt(nodes.size()));
        getContext().actorSelection(address + servicePath).tell(job, getSelf());
    }

    private void onSentenceStatisticResult(SentenceAverageResult result) {
        log.info("----" + result);
    }

    private void onSentenceStatisticFailed(SentenceStatisticFailed result) {
        log.info("----" + result);
    }

    private void onCurrentClusterState(CurrentClusterState state) {
        nodes.clear();
        state.getMembers().forEach(m -> {
            if (MemberStatus.up().equals(m.status())) {
                addNode(m);
            }
        });
    }

    private void onMemberUp(MemberUp memberUp) {
        addNode(memberUp.member());
    }

    private void addNode(Member member) {
        if (member.hasRole("compute"))
            nodes.add(member.address());
    }

    private void onReachableMember(ReachableMember reachableMember) {
        addNode(reachableMember.member());
    }

    private void onMemberEvent(MemberEvent memberEvent) {
        removeNode(memberEvent.member().address());
    }

    private void removeNode(Address address) {
        nodes.remove(address);
    }

    private void onUnreachableMember(UnreachableMember unreachableMember) {
        removeNode(unreachableMember.member().address());
    }

    @Override
    public void postStop() {
        cluster.unsubscribe(self());
        // tickTask.cancel();
    }
}
