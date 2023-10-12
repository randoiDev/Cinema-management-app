package com.cinema.notificationservice.services.imp;

import com.cinema.notificationservice.services.EmailService;
import com.cinema.notificationservice.dto.NotificationDto;
import com.cinema.notificationservice.dto.enums.NotificationType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @<code>EmailServiceImp</code> - this service is responsible for sending emails with java mail sender.
 */

@Service
@RequiredArgsConstructor
public class EmailServiceImp implements EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sentFrom;

    /**
     * This method currently is sending 3 types of notification mails:
     * <ul>
     *     <li><b>Account verification</b> - Sending a link to a rest endpoint where
     *     user can confirm his identity.</li>
     *     <li><b>Showtime reservation</b> - Sending a movie and that movies projection
     *     so user can know when the movie is starting (I could have also sent the cinema
     *     name and movie theater but this is just for demonstration purpose so no need for
     *     that much info).</li>
     *     <li><b>Reservation canceled</b> - Sending a plain text to say how sorry the
     *     company is because the reservation was cancelled.</li>
     * </ul>
     * @see NotificationDto
     * @param notificationDto
     * @implNote There are also build methods below that replace corresponding placeholders
     * with real info.There are predefined email templates that are stored in resource folder
     * of this service which are used for sending these mails.
     */
    @Override
    @Async
    public void send(NotificationDto notificationDto) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            if(notificationDto.getType() == NotificationType.ACCOUNT_VERIFICATION) {
                helper.setText(buildAccountVerificationBody(notificationDto), true);
                helper.setSubject("Account verification");
            } else if(notificationDto.getType() == NotificationType.SHOWTIME_RESERVATION) {
                helper.setText(buildReservationConfirmationBody(notificationDto), true);
                helper.setSubject("Reservation confirmation");
            } else if(notificationDto.getType() == NotificationType.RESERVATION_CANCELED) {
                helper.setText(buildReservationCanceledBody(), true);
                helper.setSubject("Reservation canceled");
            }
            helper.setTo(notificationDto.getEmail());
            helper.setFrom(sentFrom);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email");
        }
    }

    private String buildAccountVerificationBody(NotificationDto notificationDto) {
        try {
            Resource resource = new ClassPathResource("email_templates/AccountVerification.html");
            byte[] byteArray = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String content =  new String(byteArray, StandardCharsets.UTF_8);
            content = content.replace("${name}",notificationDto.getName());
            content = content.replace("${link}", notificationDto.getLink());
            return content;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to build account verification email body!");
        }
    }

    private String buildReservationConfirmationBody(NotificationDto notificationDto) {
        try {
            Resource resource = new ClassPathResource("email_templates/ReservationConfirmation.html");
            byte[] byteArray = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String content =  new String(byteArray, StandardCharsets.UTF_8);
            content = content.replace("${showtime}",notificationDto.getMovie());
            content = content.replace("${starting}", notificationDto.getStart());
            return content;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to build reservation confirmation email body!");
        }
    }

    private String buildReservationCanceledBody() {
        try {
            Resource resource = new ClassPathResource("email_templates/ReservationCanceled.html");
            byte[] byteArray = FileCopyUtils.copyToByteArray(resource.getInputStream());
            return new String(byteArray, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to build reservation canceled email body!");
        }
    }

}