package com.kmurawska.playground.akka.clusterondocker.actor;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.kmurawska.playground.akka.clusterondocker.message.FactorialJob;
import com.kmurawska.playground.akka.clusterondocker.message.FactorialResult;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public class FactorialCalculator extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void preStart() {
        log.info("FactorialCalculator actor has started successfully");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(FactorialJob.class, this::onFactorialJob)
                .build();
    }

    private void onFactorialJob(FactorialJob job) {
        CompletableFuture<FactorialResult> result = CompletableFuture
                .supplyAsync(() -> factorial(job.getNumber()))
                .thenApply(f -> new FactorialResult(job.getTrackingId(), job.getNumber(), f));

        akka.pattern.PatternsCS.pipe(result, getContext().dispatcher()).to(sender());
    }

    private BigInteger factorial(int n) {
        BigInteger acc = BigInteger.ONE;
        for (int i = 1; i <= n; ++i) {
            acc = acc.multiply(BigInteger.valueOf(i));
        }
        return acc;
    }
}