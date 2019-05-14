package spring_rabbitmq.fanout_exchange.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring_rabbitmq.fanout_exchange.FanoutExchangeMessageListener;

@Configuration
public class FanoutRabbitMqConfig {
    private static final String EXCHANGE_NAME = "com.kmurawska.playground.spring_rabbitmq.fanout_exchange";

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    public AnonymousQueue autoDeleteQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public AnonymousQueue autoDeleteQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding fanoutToQueueBinding(FanoutExchange fanout, Queue autoDeleteQueue1) {
        return BindingBuilder.bind(autoDeleteQueue1).to(fanout);
    }

    @Bean
    public Binding fanoutToQueueBinding2(FanoutExchange fanout, Queue autoDeleteQueue2) {
        return BindingBuilder.bind(autoDeleteQueue2).to(fanout);
    }

    @Bean
    public FanoutExchangeMessageListener listener() {
        return new FanoutExchangeMessageListener();
    }
}
