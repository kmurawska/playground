package spring_rabbitmq.queues;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import spring_rabbitmq.queues.config.SimpleQueueRabbitMqConfig;

@RabbitListener(queues = SimpleQueueRabbitMqConfig.QUEUE_NAME)
public class QueueMessageListener {
    @RabbitHandler
    public void onMessage(String message) {
        System.out.println(this.getClass().getName() + " " + "received message: " + message);
    }
}
