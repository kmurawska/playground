package spring_rabbitmq.fanout_exchange.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring_rabbitmq.fanout_exchange.FanoutExchangeMessageListener1;
import spring_rabbitmq.fanout_exchange.FanoutExchangeMessageListener2;

@Configuration
public class FanoutExchangeRabbitMqConfig {
    private static final String EXCHANGE_NAME = "com.kmurawska.playground.spring_rabbitmq.fanout_exchange";

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    public AnonymousQueue queue1ForFanoutExchange() {
        return new AnonymousQueue();
    }

    @Bean
    public AnonymousQueue queue2ForFanoutExchange() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding fanoutToQueueBinding(FanoutExchange fanout, Queue queue1ForFanoutExchange) {
        return BindingBuilder.bind(queue1ForFanoutExchange).to(fanout);
    }

    @Bean
    public Binding fanoutToQueueBinding2(FanoutExchange fanout, Queue queue2ForFanoutExchange) {
        return BindingBuilder.bind(queue2ForFanoutExchange).to(fanout);
    }

    @Bean
    public FanoutExchangeMessageListener1 fanoutExchangeMessageListener1() {
        return new FanoutExchangeMessageListener1();
    }

    @Bean
    public FanoutExchangeMessageListener2 fanoutExchangeMessageListener2() {
        return new FanoutExchangeMessageListener2();
    }
}
