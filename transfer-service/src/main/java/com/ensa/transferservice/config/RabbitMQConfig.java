package com.ensa.transferservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //jackson converter
    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange("notificationExchange");
    }

    @Bean
    public Queue msgNotificationQueue() {
        return new Queue("msgQueue");
    }

    @Bean
    public Queue otpNotificationQueue() {
        return new Queue("otpQueue");
    }

    @Bean
    public Binding bindEmailQueueToExchange(Queue msgNotificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(msgNotificationQueue).to(notificationExchange).with("msgRoutingKey");
    }

    @Bean
    public Binding bindOTPQueueToExchange(Queue otpNotificationQueue, DirectExchange notificationExchange) {
        return BindingBuilder.bind(otpNotificationQueue).to(notificationExchange).with("otpRoutingKey");
    }
}

