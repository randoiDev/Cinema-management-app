package com.cinema.movieshowtimeservice.domain;

import com.cinema.movieshowtimeservice.domain.enums.SeatType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @<code>Seat</code> - Entity that represents cinema movie theater seat.
 * @implNote Since this entity is a weakly identifying entity, it's primary key
 * consists of cinema's id, movie theaters number and its seat number.
 * @see SeatPK
 * Attributes:
 * <ul>
 *     <li><code>seat</code> - Seat type</li>
 *     <li><code>reservations</code> - Reservations that correspond to this seat</li>
 * </ul>
 * @see SeatType
 */

@SuppressWarnings("all")
@Entity
@Table(name = "seat")
@Data
@NoArgsConstructor
public class Seat {

    @EmbeddedId
    private SeatPK seatPK;
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private SeatType seat;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "seats")
    private Collection<Reservation> reservations = new ArrayList<>();
    //I specified explicitly cinema and movie theater number here like this to avoid hibernate's naming of fk columns
    @ManyToOne
    @MapsId("movieTheaterPK")
    @JoinColumns(
            {
                    @JoinColumn(name = "cinema_id", referencedColumnName = "cinema_id"),
                    @JoinColumn(name = "mt_number", referencedColumnName = "mt_number")
            }
    )
    private MovieTheater movieTheater;

}
