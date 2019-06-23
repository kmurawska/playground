package rabbitmq.control.fanout_exchange;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FanoutExchangeMessageProducer {
    private static final String ROUTING_KEY = "";
    private final RabbitTemplate rabbitTemplate;
    private final FanoutExchange exchange;

    public FanoutExchangeMessageProducer(RabbitTemplate rabbitTemplate, FanoutExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void sendRandomMessage() {
        String message = UUID.randomUUID().toString();
        System.out.println("Sending message...: " + message);
        rabbitTemplate.convertAndSend(exchange.getName(), ROUTING_KEY, message);
    }
}
