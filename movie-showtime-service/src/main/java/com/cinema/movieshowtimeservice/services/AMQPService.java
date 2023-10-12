package com.cinema.movieshowtimeservice.services;


import com.cinema.movieshowtimeservice.dto.NotificationDto;

public interface AMQPService {

    void sendMessage(NotificationDto notification);

}
