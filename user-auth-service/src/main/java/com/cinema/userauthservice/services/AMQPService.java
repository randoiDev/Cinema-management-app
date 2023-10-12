package com.cinema.userauthservice.services;

import com.cinema.userauthservice.dto.NotificationDto;

public interface AMQPService {

    void sendMessage(NotificationDto notification);

}
