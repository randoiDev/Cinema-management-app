package com.cinema.movieshowtimeservice.dto;

import com.cinema.movieshowtimeservice.dto.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {

    private NotificationType type;
    private String name;
    private String email;
    private String link;
    private String movie;
    private String start;

}
