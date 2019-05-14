package spring_rabbitmq.queues;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import spring_rabbitmq.queues.config.SimpleQueueRabbitMqConfig;

import java.util.UUID;

@Component
public class QueueMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public QueueMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendRandomMessage() {
        String message = UUID.randomUUID().toString();
        System.out.println("Sending message...: " + message);
        rabbitTemplate.convertAndSend(SimpleQueueRabbitMqConfig.QUEUE_NAME, message);
    }
}