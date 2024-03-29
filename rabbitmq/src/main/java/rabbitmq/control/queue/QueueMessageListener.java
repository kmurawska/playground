package rabbitmq.control.queue;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import rabbitmq.configuration.NamedQueueRabbitMqConfig;

@RabbitListener(queues = NamedQueueRabbitMqConfig.QUEUE_NAME_1)
public class QueueMessageListener {
    @RabbitHandler
    public void onMessage(String message) {
        System.out.println(this.getClass().getName() + " " + "received message: " + message);
    }
}
