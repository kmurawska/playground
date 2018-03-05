package com.kmurawska.playground.akka.transformation.actor;

import akka.actor.AbstractActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.CurrentClusterState;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akka.transformation.message.TransformationJob;
import com.kmurawska.playground.akka.transformation.message.TransformationResult;
import com.kmurawska.playground.akka.transformation.message.WorkerRegistration;

public class TransformationWorker extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private Cluster cluster = Cluster.get(getContext().system());

    @Override
    public void preStart() {
        cluster.subscribe(self(), MemberUp.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MemberUp.class, this::onMemberUp)
                .match(TransformationJob.class, this::onTransformationJob)
                .match(CurrentClusterState.class, this::onCurrentClusterState)
                .build();
    }

    private void onMemberUp(MemberUp memberUp) {
        registerMember(memberUp.member());
    }

    private void registerMember(Member member) {
        getContext().actorSelection(member.address() + "/user/transformation-client").tell(new WorkerRegistration(), self());

    }

    private void onTransformationJob(TransformationJob job) {
        log.info("---" + job);
        getSender().tell(new TransformationResult(job.getTrackingId(), transform(job.getText())), getSelf());
    }

    private String transform(String text) {
        return text.toUpperCase();
    }

    private void onCurrentClusterState(CurrentClusterState state) {
        state.getMembers().forEach(m -> {
            if (MemberStatus.up().equals(m.status())) {
                registerMember(m);
            }
        });
    }

    @Override
    public void postStop() {
        cluster.unsubscribe(self());
    }
}