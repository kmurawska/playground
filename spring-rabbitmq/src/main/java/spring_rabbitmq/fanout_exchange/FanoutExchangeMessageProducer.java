package spring_rabbitmq.fanout_exchange;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FanoutExchangeMessageProducer {
    private static final String ROUTING_KEY = "";
    private final RabbitTemplate rabbitTemplate;
    private final FanoutExchange fanout;

    public FanoutExchangeMessageProducer(RabbitTemplate rabbitTemplate, FanoutExchange fanout) {
        this.rabbitTemplate = rabbitTemplate;
        this.fanout = fanout;
    }

    public void sendRandomMessage() {
        String message = UUID.randomUUID().toString();
        System.out.println("Sending message...: " + message);
        rabbitTemplate.convertAndSend(fanout.getName(), ROUTING_KEY, message);
    }
}
