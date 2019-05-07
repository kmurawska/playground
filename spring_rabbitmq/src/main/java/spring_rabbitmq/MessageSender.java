package spring_rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MessageSender implements CommandLineRunner {
    static final String ROUTING_KEY = "com.kmurawska.playground.spring_rabbitmq.MessageSender";
    private final RabbitTemplate rabbitTemplate;

    public MessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void run(String... args) {
        System.out.println("Sending message...");
        rabbitTemplate.convertAndSend(
                Application.TOPIC_NAME,
                ROUTING_KEY + MessageSender.class.getName(),
                "Message: " + UUID.randomUUID().toString() + " with routing key: " + ROUTING_KEY
        );
    }
}