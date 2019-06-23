package rabbitmq.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rabbitmq.control.fanout_exchange.FanoutExchangeMessageListener1;
import rabbitmq.control.fanout_exchange.FanoutExchangeMessageListener2;

@Configuration
public class FanoutExchangeRabbitMqConfig {
    private static final String EXCHANGE_NAME = "FANOUT_EXCHANGE";

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

    @Bean
    public AnonymousQueue temporaryQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public AnonymousQueue temporaryQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding fanoutToQueue1Binding(FanoutExchange fanout, Queue temporaryQueue1) {
        return BindingBuilder.bind(temporaryQueue1).to(fanout);
    }

    @Bean
    public Binding fanoutToQueue2Binding(FanoutExchange fanout, Queue temporaryQueue2) {
        return BindingBuilder.bind(temporaryQueue2).to(fanout);
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
