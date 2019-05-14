package spring_rabbitmq.queues.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring_rabbitmq.queues.QueueMessageListener;

@Configuration
public class SimpleQueueRabbitMqConfig {
    public static final String QUEUE_NAME = "com.kmurawska.playground.spring_rabbitmq.queue";

    @Bean
    Queue namedQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    QueueMessageListener namedQueueMessageListener() {
        return new QueueMessageListener();
    }
}
