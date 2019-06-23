package rabbitmq.control.queue;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import rabbitmq.configuration.QueueRabbitMqConfig;

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
        rabbitTemplate.convertAndSend(QueueRabbitMqConfig.QUEUE_NAME_1, message);
        rabbitTemplate.convertAndSend(QueueRabbitMqConfig.QUEUE_NAME_2, message);
    }
}