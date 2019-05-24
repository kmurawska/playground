package spring_rabbitmq.fanout_exchange;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class FanoutExchangeMessageListener2 {
    @RabbitListener(queues = "#{queue2ForFanoutExchange.name}")
    public void onMessage(String message) {
        System.out.println(this.getClass().getName() + " " + "received message: " + message);
    }
}
