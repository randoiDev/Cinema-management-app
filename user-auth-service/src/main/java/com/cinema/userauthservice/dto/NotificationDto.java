package com.cinema.userauthservice.dto;

import com.cinema.userauthservice.dto.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

/**
 * <code>NotificationDto</code> - Class that represents type of notification that is going to be sent through
 * message queue to a notification service that is responsible for processing them and sending appropriate mails
 * to user gmail accounts.Notification types are:
 * <ul>
 *     <li><b>ACCOUNT_VERIFICATION</b> - for sending mails to confirm verification</li>
 *     <li><b>SHOWTIME_RESERVATION</b> - for sending mails about created reservation</li>
 *     <li><b>RESERVATION_CANCELED</b> - for sending mails about canceling reservation</li>
 * </ul>
 * @implNote Not every field except type needs to be set for every notification.
 * Example: For account verification its only necessary to set name,email and link.
 */

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
