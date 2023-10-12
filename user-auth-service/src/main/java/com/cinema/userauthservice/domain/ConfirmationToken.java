package com.cinema.userauthservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @<code>ConfirmationToken</code> - Entity that represents a token for user account verification.
 * Attributes:
 * <ul>
 *     <li><code>token</code> - Actual UUID string representation name of a token</li>
 *     <li><code>createdAt</code> - Date and time when token was created</li>
 *     <li><code>expiresAt</code> - Date and time when token will expire if user account verification is not confirmed</li>
 *     <li><code>confirmedAt</code> - Date and time when user account was verified</li>
 *     <li><code>user</code> - User id that refers to a record in user table</li>
 * </ul>
 */
@SuppressWarnings("all")
@Entity
@Table(name = "confirmation_token")
@Data
@NoArgsConstructor
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false,name = "created_at")
    private LocalDateTime createdAt;
    @Column(nullable = false,name = "expires_at")
    private LocalDateTime expiresAt;
    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;
    @ManyToOne(optional = false)
    private User user;

}
