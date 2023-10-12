package com.cinema.movieshowtimeservice.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @<code>Ticket</code> - Entity that represents an showtime ticket.
 * Attributes:
 * <ul>
 *     <li><code>price</code> - Ticket price</li>
 *     <li><code>purchaseDate</code> - Date when ticket was bought</li>
 *     <li><code>seat</code> - Seat for which ticket is bought</li>
 *     <li><code>show</code> - Show for which this ticket is</li>
 * </ul>
 */

@SuppressWarnings("all")
@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;
    @Column(nullable = false)
    private Integer price;
    @Column(name = "purchase_date", nullable = false)
    private Date purchaseDate;
    @OneToOne
    @JoinColumns(
            {
                    @JoinColumn(name = "cinema_id", referencedColumnName = "cinema_id", nullable = false),
                    @JoinColumn(name = "mt_number", referencedColumnName = "mt_number", nullable = false),
                    @JoinColumn(name = "seat_number", referencedColumnName = "seat_number", nullable = false)
            }
    )
    private Seat seat;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime show;

}
