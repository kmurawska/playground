package rabbitmq.control.topic_exchange;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class TopicExchangeMessageListener1 {
    @RabbitListener(queues = "#{queue1ForTopicExchange.name}")
    public void onMessage(String message) {
        System.out.println(this.getClass().getName() + " received message: " + message);
    }
}
