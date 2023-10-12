package com.cinema.movieshowtimeservice.domain;

import com.cinema.movieshowtimeservice.domain.enums.ShowtimeState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @<code>Showtime</code> - Entity that represents movie projection.
 * @implNote This entity is also unique by combination of the specified
 * fields in unique constraint annotation below.
 * Attributes:
 * <ul>
 *     <li><code>economyPrice</code> - Price for cheap seats</li>
 *     <li><code>vipPrice</code> - Price for expensive seats</li>
 *     <li><code>showtimeDate</code> - Projection time</li>
 *     <li><code>movieTheater</code> - Movie theater in which projection will take place</li>
 *     <li><code>tickets</code> - Sold tickets for this showtime</li>
 *     <li><code>reservations</code> - Reservations for this showtime</li>
 * </ul>
 * @see ShowtimeState
 */

@SuppressWarnings("all")
@Entity
@Table(name = "showtime",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"cinema_id", "mt_number", "showtime_date"})
        })
@Data
@NoArgsConstructor
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "showtime_id", nullable = false)
    private Long showtimeId;
    @Column(name = "economy_price", nullable = false)
    private Integer economyPrice;
    @Column(name = "vip_price", nullable = false)
    private Integer vipPrice;
    @Column(name = "showtime_date", nullable = false)
    private Timestamp showtimeDate;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumns(
            {
                    @JoinColumn(name = "cinema_id", referencedColumnName = "cinema_id"),
                    @JoinColumn(name = "mt_number", referencedColumnName = "mt_number")
            }
    )
    private MovieTheater movieTheater;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ShowtimeState state = ShowtimeState.CREATION;
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "show")
    private Collection<Ticket> tickets = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "show")
    private Collection<Reservation> reservations = new ArrayList<>();

}
