package rabbitmq.control.fanout_exchange;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class FanoutExchangeMessageListener1 {
    @RabbitListener(queues = "#{temporaryQueue1.name}")
    public void onMessage(String message) {
        System.out.println(this.getClass().getName() + " " + "received message: " + message);
    }
}
