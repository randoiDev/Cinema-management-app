package com.cinema.movieshowtimeservice.domain;

import com.cinema.movieshowtimeservice.domain.enums.ShowtimeState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @<code>Reservation</code> - Entity that represents show reservation.
 * Attributes:
 * <ul>
 *     <li><code>reservationDate</code> - Date when the reservation was made</li>
 *     <li><code>userEmail</code> - Email of the user that made the reservation</li>
 *     <li><code>show</code> - Show for which the reservation was made</li>
 *     <li><code>seats</code> - Seats that are included in the reservation</li>
 * </ul>
 */

@SuppressWarnings("all")
@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;
    @Column(name = "reservation_date", nullable = false)
    private Timestamp reservationDate;
    @Column(name = "user_id", nullable = false)
    private String userEmail;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    private Showtime show;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(
            name = "reservation_seat",
            joinColumns = @JoinColumn(name = "reservation_id", referencedColumnName = "reservation_id"),
            inverseJoinColumns = {
                    @JoinColumn(name = "cinema_id", referencedColumnName = "cinema_id"),
                    @JoinColumn(name = "mt_number", referencedColumnName = "mt_number"),
                    @JoinColumn(name = "seat_number", referencedColumnName = "seat_number")
            }
    )
    private Collection<Seat> seats = new ArrayList<>();

}
