package rabbitmq.control.direct_exchange;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class DirectExchangeMessageListener1 {
    @RabbitListener(queues = "#{queue1ForDirectExchange.name}")
    public void onMessage(String message) {
        System.out.println(this.getClass().getName() + " received message: " + message);
    }
}
