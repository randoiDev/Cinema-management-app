package com.cinema.movieshowtimeservice.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @<code>MovieTheater</code> - Entity that represents cinema movie theater.
 * @implNote Since this entity is a weakly identifying entity, it's primary key
 * consists of movie theater number and its cinema's id.
 * @see MovieTheaterPK
 * Attributes:
 * <ul>
 *     <li><code>shows</code> - Shows that will take place in this movie theater</li>
 *     <li><code>seats</code> - Seats that belong to this movie theater</li>
 * </ul>
 */

@SuppressWarnings("all")
@Entity
@Table(name = "movie_theater")
@Data
@NoArgsConstructor
public class MovieTheater {

    @EmbeddedId
    private MovieTheaterPK movieTheaterPK;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "movieTheater")
    private Collection<Showtime> shows = new ArrayList<>();
    //I specified explicitly cinema here like this to avoid hibernate's naming of fk columns
    @ManyToOne
    @MapsId("cinemaId")
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "movieTheater")
    private Collection<Seat> seats = new ArrayList<>();

}
