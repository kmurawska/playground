package spring_rabbitmq.topic_exchange;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class TopicExchangeMessageListener2 {
    @RabbitListener(queues = "#{queue2ForTopicExchange.name}")
    public void onMessage(String message) {
        System.out.println(this.getClass().getName() + " received message: " + message);
    }
}
