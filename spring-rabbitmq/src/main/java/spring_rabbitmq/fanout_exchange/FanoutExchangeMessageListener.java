package spring_rabbitmq.fanout_exchange;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class FanoutExchangeMessageListener {

    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void onMessageQueue1(String message) {
        handle(1, message);
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void onMessageQueue2(String message) {
        handle(2, message);
    }

    private void handle(int listenerNo, String message) {
        System.out.println("Listener: " + listenerNo + " received message: " + message);
    }
}
