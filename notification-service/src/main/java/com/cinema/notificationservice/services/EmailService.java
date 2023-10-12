package com.cinema.notificationservice.services;

import com.cinema.notificationservice.dto.NotificationDto;

public interface EmailService {
    void send(NotificationDto notification);
}
