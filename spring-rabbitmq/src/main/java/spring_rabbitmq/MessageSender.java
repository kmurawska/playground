package spring_rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import spring_rabbitmq.config.RabbitMqConfig;

import java.util.UUID;

@Component
public class MessageSender {
    private final RabbitTemplate rabbitTemplate;

    public MessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    void sendRandomMessage() {
        String message = "Message: " + UUID.randomUUID().toString() + " with routing key: " + RabbitMqConfig.ROUTING_KEY;
        System.out.println("Sending message...: " + message);
        rabbitTemplate.convertAndSend(RabbitMqConfig.TOPIC_NAME, RabbitMqConfig.ROUTING_KEY, message);
    }
}