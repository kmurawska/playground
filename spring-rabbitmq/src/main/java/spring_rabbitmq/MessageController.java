package spring_rabbitmq;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring_rabbitmq.direct_exchange.Color;
import spring_rabbitmq.direct_exchange.DirectExchangeMessageProducer;
import spring_rabbitmq.fanout_exchange.FanoutExchangeMessageProducer;
import spring_rabbitmq.queues.QueueMessageProducer;

@RestController
public class MessageController {
    private final QueueMessageProducer queueMessageProducer;
    private final FanoutExchangeMessageProducer fanoutExchangeMessageProducer;
    private final DirectExchangeMessageProducer directExchangeMessageProducer;

    public MessageController(QueueMessageProducer queueMessageProducer, FanoutExchangeMessageProducer fanoutExchangeMessageProducer, DirectExchangeMessageProducer directExchangeMessageProducer) {
        this.queueMessageProducer = queueMessageProducer;
        this.fanoutExchangeMessageProducer = fanoutExchangeMessageProducer;
        this.directExchangeMessageProducer = directExchangeMessageProducer;
    }

    @GetMapping("/queue")
    public void sendRandomMessage() {
        queueMessageProducer.sendRandomMessage();
    }

    @GetMapping("/fanout-exchange")
    public void sendRandomMessageToFanoutQueue() {
        fanoutExchangeMessageProducer.sendRandomMessage();
    }

    @GetMapping("/direct-exchange/{routing-key}")
    public void sendRandomMessageToDirectExchange(@PathVariable("routing-key") Color routingKey) {
        directExchangeMessageProducer.sendRandomMessageWithRoutingKey(routingKey);
    }
}
