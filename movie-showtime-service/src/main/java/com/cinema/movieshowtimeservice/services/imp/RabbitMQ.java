package com.cinema.movieshowtimeservice.services.imp;

import com.cinema.movieshowtimeservice.dto.NotificationDto;
import com.cinema.movieshowtimeservice.services.AMQPService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @<code>RabbitMQ</code> - This class is responsible for sending messages to a specified queue with a specified routing key
 */

@Service
@RequiredArgsConstructor
public class RabbitMQ implements AMQPService {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing_key.name}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(NotificationDto notification) {
        try {
            rabbitTemplate.convertAndSend(exchange,routingKey,notification);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }
}
