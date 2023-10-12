package com.cinema.notificationservice.services;

import com.cinema.notificationservice.dto.NotificationDto;

public interface AMQPService {

    void consumeMessage(NotificationDto notification);

}
