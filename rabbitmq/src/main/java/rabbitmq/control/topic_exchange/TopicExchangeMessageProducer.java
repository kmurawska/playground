package rabbitmq.control.topic_exchange;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TopicExchangeMessageProducer {
    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange exchange;

    public TopicExchangeMessageProducer(RabbitTemplate rabbitTemplate, TopicExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void sendRandomMessageWithRoutingKey(String routingKey) {
        String message = routingKey + " - " + UUID.randomUUID().toString();
        System.out.println("Sending message...: " + message);
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, message);
    }
}
