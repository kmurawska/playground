package spring_rabbitmq;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring_rabbitmq.fanout_exchange.FanoutExchangeMessageProducer;
import spring_rabbitmq.queues.QueueMessageProducer;

@RestController
public class MessageController {
    private final QueueMessageProducer queueMessageProducer;
    private final FanoutExchangeMessageProducer fanoutExchangeMessageProducer;

    public MessageController(QueueMessageProducer queueMessageProducer, FanoutExchangeMessageProducer fanoutExchangeMessageProducer) {
        this.queueMessageProducer = queueMessageProducer;
        this.fanoutExchangeMessageProducer = fanoutExchangeMessageProducer;
    }

    @GetMapping("/queue")
    public void sendRandomMessage() {
        queueMessageProducer.sendRandomMessage();
    }

    @GetMapping("/fanout-exchange")
    public void sendRandomMessageToWorkQueue() {
        fanoutExchangeMessageProducer.sendRandomMessage();
    }
}
