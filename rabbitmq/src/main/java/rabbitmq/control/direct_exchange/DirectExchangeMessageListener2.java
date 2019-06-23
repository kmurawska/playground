package rabbitmq.control.direct_exchange;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class DirectExchangeMessageListener2 {
    @RabbitListener(queues = "#{queue2ForDirectExchange.name}")
    public void onMessage(String message) {
        System.out.println(this.getClass().getName() + " received message: " + message);
    }
}
