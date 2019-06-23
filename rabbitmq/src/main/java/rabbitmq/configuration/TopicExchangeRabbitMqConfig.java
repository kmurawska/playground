package rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rabbitmq.control.topic_exchange.TopicExchangeMessageListener1;
import rabbitmq.control.topic_exchange.TopicExchangeMessageListener2;

@Configuration
public class TopicExchangeRabbitMqConfig {
    private static final String EXCHANGE_NAME = "TOPIC_EXCHANGE";

    @Bean
    public TopicExchange topic() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public AnonymousQueue queue1ForTopicExchange() {
        return new AnonymousQueue();
    }

    @Bean
    public AnonymousQueue queue2ForTopicExchange() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding queue1BindingForOrangeAnimals(TopicExchange exchange, Queue queue1ForTopicExchange) {
        return BindingBuilder.bind(queue1ForTopicExchange)
                .to(exchange)
                .with("*.orange.*");
    }

    @Bean
    public Binding queue1BindingForRabbit(TopicExchange exchange, Queue queue1ForTopicExchange) {
        return BindingBuilder.bind(queue1ForTopicExchange)
                .to(exchange)
                .with("*.*.rabbit");
    }

    @Bean
    public Binding queue2BindingForLazyAnimals(TopicExchange exchange, Queue queue2ForTopicExchange) {
        return BindingBuilder.bind(queue2ForTopicExchange)
                .to(exchange)
                .with("lazy.*.*");
    }

    @Bean
    public TopicExchangeMessageListener1 topicExchangeMessageListener1() {
        return new TopicExchangeMessageListener1();
    }

    @Bean
    public TopicExchangeMessageListener2 topicExchangeMessageListener2() {
        return new TopicExchangeMessageListener2();
    }

}
