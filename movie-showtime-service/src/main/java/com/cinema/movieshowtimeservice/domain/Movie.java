package com.cinema.movieshowtimeservice.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @<code>Movie</code> - Entity that represents a movie.
 * Attributes:
 * <ul>
 *     <li><code>title</code> - Movie title</li>
 *     <li><code>description</code> - Movie description</li>
 *     <li><code>genre</code> - Movie genre</li>
 *     <li><code>releaseDate</code> - Movie's release date</li>
 *     <li><code>duration</code> - Movie duration</li>
 *     <li><code>shows</code> - Movie showtimes</li>
 *     <li><code>movieRatings</code> - Movie ratings</li>
 *     <li><code>deleted</code> - Movie soft delete indicator</li>
 * </ul>
 */

@SuppressWarnings("all")
@Entity
@Table(name = "movie")
@Data
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id", nullable = false)
    private Long movieId;
    @Column(length = 100, nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(length = 20, nullable = false)
    private String genre;
    @Column(name = "release_date", nullable = false)
    private Date releaseDate;
    @Column(nullable = false)
    private Integer duration;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
    private Collection<Showtime> shows = new ArrayList<>();
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "movie")
    private Collection<MovieRating> movieRatings = new ArrayList<>();
    private boolean deleted = false;

}
