package rabbitmq.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rabbitmq.control.queue.QueueMessageListener;

@Configuration
public class QueueRabbitMqConfig {
    public static final String QUEUE_NAME_1 = "QUEUE_1";
    public static final String QUEUE_NAME_2 = "QUEUE_2";

    @Bean
    Queue namedQueue1() {
        return new Queue(QUEUE_NAME_1, false);
    }

    @Bean
    Queue namedQueue2() {
        return new Queue(QUEUE_NAME_2, false);
    }

    @Bean
    QueueMessageListener namedQueueMessageListener() {
        return new QueueMessageListener();
    }
}
