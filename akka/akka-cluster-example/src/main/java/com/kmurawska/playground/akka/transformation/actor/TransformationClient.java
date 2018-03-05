package com.kmurawska.playground.akka.transformation.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akka.transformation.message.TransformationFailed;
import com.kmurawska.playground.akka.transformation.message.TransformationJob;
import com.kmurawska.playground.akka.transformation.message.TransformationResult;
import com.kmurawska.playground.akka.transformation.message.WorkerRegistration;

import java.util.ArrayList;
import java.util.List;

public class TransformationClient extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private List<ActorRef> workers = new ArrayList<>();
    private int jobCounter = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(WorkerRegistration.class, this::onWorkerRegistration)
                .match(TransformationJob.class, j -> workers.isEmpty(), this::onTransformationJobWhenNoWorkers)
                .match(TransformationJob.class, this::onTransformationJob)
                .match(TransformationResult.class, this::onTransformationResult)
                .match(Terminated.class, this::onTerminated)
                .build();

    }

    private void onWorkerRegistration(WorkerRegistration registration) {
        getContext().watch(getSender());
        workers.add(getSender());
    }

    private void onTransformationJobWhenNoWorkers(TransformationJob job) {
        sender().tell(new TransformationFailed("Service unavailable, try again later", job), sender());
    }

    private void onTransformationJob(TransformationJob job) {
        jobCounter++;
        workers.get(jobCounter % workers.size()).forward(job, getContext());
    }

    private void onTransformationResult(TransformationResult result) {
        log.info("---" + result);
    }

    private void onTerminated(Terminated terminated) {
        workers.remove(terminated);
    }
}