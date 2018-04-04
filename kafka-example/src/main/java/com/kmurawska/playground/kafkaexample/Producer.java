package com.kmurawska.playground.kafkaexample;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.Properties;

public class Producer {

    public static void main(String[] args) throws IOException {
        Properties p = new Properties();
        p.load(ClassLoader.getSystemResourceAsStream("producer.properties"));
        KafkaProducer<String, String> producer = new KafkaProducer<>(p);

        producer.send(new ProducerRecord<>("fast-messages", "---" + System.nanoTime()));
    }
}
