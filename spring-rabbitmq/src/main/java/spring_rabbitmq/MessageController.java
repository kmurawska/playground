package spring_rabbitmq;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import spring_rabbitmq.direct_exchange.Color;
import spring_rabbitmq.direct_exchange.DirectExchangeMessageProducer;
import spring_rabbitmq.fanout_exchange.FanoutExchangeMessageProducer;
import spring_rabbitmq.queues.QueueMessageProducer;
import spring_rabbitmq.topic_exchange.TopicExchangeMessageProducer;

@RestController
public class MessageController {
    private final QueueMessageProducer queueMessageProducer;
    private final FanoutExchangeMessageProducer fanoutExchangeMessageProducer;
    private final DirectExchangeMessageProducer directExchangeMessageProducer;
    private final TopicExchangeMessageProducer topicExchangeMessageProducer;

    public MessageController(QueueMessageProducer queueMessageProducer, FanoutExchangeMessageProducer fanoutExchangeMessageProducer, DirectExchangeMessageProducer directExchangeMessageProducer, TopicExchangeMessageProducer topicExchangeMessageProducer) {
        this.queueMessageProducer = queueMessageProducer;
        this.fanoutExchangeMessageProducer = fanoutExchangeMessageProducer;
        this.directExchangeMessageProducer = directExchangeMessageProducer;
        this.topicExchangeMessageProducer = topicExchangeMessageProducer;
    }

    @GetMapping("/queue")
    public void sendRandomMessage() {
        queueMessageProducer.sendRandomMessage();
    }

    @GetMapping("/fanout-exchange")
    public void sendRandomMessageToFanoutQueue() {
        fanoutExchangeMessageProducer.sendRandomMessage();
    }

    @GetMapping("/direct-exchange/routing-key={routing-key}")
    public void sendRandomMessageToDirectExchange(@PathVariable("routing-key") Color routingKey) {
        directExchangeMessageProducer.sendRandomMessageWithRoutingKey(routingKey);
    }

    @GetMapping("/topic-exchange/routing-key={routing-key}")
    public void sendRandomMessageToTopicExchange(@PathVariable("routing-key") String routingKey) {
        topicExchangeMessageProducer.sendRandomMessageWithRoutingKey(routingKey);
    }
}
