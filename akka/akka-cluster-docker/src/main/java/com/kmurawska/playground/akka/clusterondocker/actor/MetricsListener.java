package com.kmurawska.playground.akka.clusterondocker.actor;

import akka.actor.AbstractActor;
import akka.cluster.Cluster;
import akka.cluster.metrics.ClusterMetricsChanged;
import akka.cluster.metrics.ClusterMetricsExtension;
import akka.cluster.metrics.NodeMetrics;
import akka.cluster.metrics.StandardMetrics;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MetricsListener extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private final ClusterMetricsExtension clusterMetricsExtension = ClusterMetricsExtension.get(getContext().system());
    private final Cluster cluster = Cluster.get(getContext().system());

    @Override
    public void preStart() {
        clusterMetricsExtension.subscribe(getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ClusterMetricsChanged.class, this::onClusterMetricsChanged)
                .build();
    }

    private void onClusterMetricsChanged(ClusterMetricsChanged change) {
        change.getNodeMetrics().forEach(n -> {
            if (n.address().equals(cluster.selfAddress())) {
                logHeap(n);
                logCpu(n);
            }
        });
    }

    private void logHeap(NodeMetrics nodeMetrics) {
        StandardMetrics.HeapMemory heap = StandardMetrics.extractHeapMemory(nodeMetrics);
        if (heap != null) {
            log.info("Used heap: {} MB", ((double) heap.used()) / 1024 / 1024);
        }
    }

    private void logCpu(NodeMetrics nodeMetrics) {
        StandardMetrics.Cpu cpu = StandardMetrics.extractCpu(nodeMetrics);
        if (cpu != null) {
            log.info("Load: {} ({} processors)", cpu.systemLoadAverage().get(), cpu.processors());
        }
    }

    @Override
    public void postStop() {
        clusterMetricsExtension.unsubscribe(getSelf());
    }
}
