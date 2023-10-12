package com.cinema.notificationservice.dto;

import com.cinema.notificationservice.dto.enums.NotificationType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationDto {

    private NotificationType type;
    private String name;
    private String email;
    private String link;
    private String movie;
    private String start;

}
