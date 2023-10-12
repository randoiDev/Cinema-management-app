package com.cinema.notificationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * All of the following variables and functions are for
 * advanced messaging queuing protocol asynchronous communication using
 * rabbitMQ as its implementation.The key components here are:
 * <ul>
 *     <li><code>Queue</code>-storage where producers post messages and consumers acquire them and acknowledge their consumption</li>
 *     <li><code>Exchange</code>-mechanism for routing messages to targeted queue (there can be more queue's in usage</li>
 *     <li><code>Routing key</code>-access key for a specific queue, exchange mechanism based on it knows where to route messages</li>
 * </ul>
 * <code>bind()</code> - this method is responsible for binding a queue to an exchange object with routing key.
 * <code>converter()</code> - this method is providing a concrete message converter which is used to serialize
 * a message to a form compatible for sending such as <i>JSON</i> format.
 * <code>amqpTemplate()</code> - method for setting up connection factory that is going to be used to create connections to
 * rabbitMQ service (in this case simple connection factory) and setting an implementation of a message converter
 * (in this case jackson 2 json message converter)
 * @return RabbitTemplate instance
 */

@Configuration
public class AppConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing_key.name}")
    private String routingKey;

    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding bind() {
        return BindingBuilder
                .bind(queue())
                .to(exchange())
                .with(routingKey);
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
