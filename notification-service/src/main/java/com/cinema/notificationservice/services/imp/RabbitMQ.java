package com.cinema.notificationservice.services.imp;

import com.cinema.notificationservice.dto.NotificationDto;
import com.cinema.notificationservice.services.AMQPService;
import com.cinema.notificationservice.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @<code>RabbitMQ</code> - This class is responsible for consuming messages from a specified queue
 */

@Service
@RequiredArgsConstructor
public class RabbitMQ implements AMQPService {

    private final EmailService emailService;

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    @Override
    public void consumeMessage(NotificationDto notification) {
        emailService.send(notification);
    }
}
