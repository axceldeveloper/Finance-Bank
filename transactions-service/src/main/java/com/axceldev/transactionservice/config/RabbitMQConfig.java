package com.axceldev.transactionservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.transaction.queue}")
    private String transactionQueue;

    @Value("${app.transaction.exchange}")
    private String transactionExchange;

    @Value("${app.transaction.routing-key}")
    private String transactionRoutingKey;

    @Bean
    public Queue depositInterBankQueue() {
        return new Queue(transactionQueue);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public TopicExchange depositInterBankExchange() {
        return new TopicExchange(transactionExchange);
    }

    @Bean
    public Binding binding(Queue depositInterBankQueue, TopicExchange depositInterBankExchange) {
        return BindingBuilder
                .bind(depositInterBankQueue)
                .to(depositInterBankExchange)
                .with(transactionRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
