package spring_rabbitmq.direct_exchange.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring_rabbitmq.direct_exchange.Color;
import spring_rabbitmq.direct_exchange.DirectExchangeMessageListener1;
import spring_rabbitmq.direct_exchange.DirectExchangeMessageListener2;

@Configuration
public class DirectExchangeRabbitMqConfig {
    private static final String EXCHANGE_NAME = "com.kmurawska.playground.spring_rabbitmq.direct_exchange";

    @Bean
    public DirectExchange direct() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public AnonymousQueue queue1ForDirectExchange() {
        return new AnonymousQueue();
    }

    @Bean
    public AnonymousQueue queue2ForDirectExchange() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding exchangeToQueue1BindingForOrangeMessages(DirectExchange exchange, Queue queue1ForDirectExchange) {
        return BindingBuilder.bind(queue1ForDirectExchange)
                .to(exchange)
                .with(Color.ORANGE.name());
    }

    @Bean
    public Binding exchangeToQueue1BindingForBlackMessages(DirectExchange exchange, Queue queue1ForDirectExchange) {
        return BindingBuilder.bind(queue1ForDirectExchange)
                .to(exchange)
                .with(Color.BLACK.name());
    }

    @Bean
    public Binding exchangeToQueue2BindingForGreenMessages(DirectExchange exchange, Queue queue2ForDirectExchange) {
        return BindingBuilder.bind(queue2ForDirectExchange)
                .to(exchange)
                .with(Color.GREEN.name());
    }

    @Bean
    public Binding exchangeToQueue2BindingForBlackMessages(DirectExchange exchange, Queue queue2ForDirectExchange) {
        return BindingBuilder.bind(queue2ForDirectExchange)
                .to(exchange)
                .with(Color.BLACK.name());
    }

    @Bean
    public DirectExchangeMessageListener1 directExchangeMessageListener1() {
        return new DirectExchangeMessageListener1();
    }

    @Bean
    public DirectExchangeMessageListener2 directExchangeMessageListener2() {
        return new DirectExchangeMessageListener2();
    }
}
