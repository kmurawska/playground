package rabbitmq.control.direct_exchange;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DirectExchangeMessageProducer {
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange exchange;

    public DirectExchangeMessageProducer(RabbitTemplate rabbitTemplate, DirectExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void sendRandomMessageWithRoutingKey(Color routingKey) {
        String message = routingKey + " - " + UUID.randomUUID().toString();
        System.out.println("Sending message...: " + message);
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey.name(), message);
    }
}
