package com.cinema.movieshowtimeservice.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @<code>MovieRating</code> - Entity that represents a movie rating.
 * Attributes:
 * <ul>
 *     <li><code>comment</code> - User comment on a movie</li>
 *     <li><code>rating</code> - Movie rating from 0 to 10</li>
 *     <li><code>reviewDate</code> - Date on which movie rating was given</li>
 *     <li><code>userEmail</code> - Email of a user that gave this movie rating</li>
 *     <li><code>movie</code> - Movie that is owner of this rating</li>
 * </ul>
 */

@SuppressWarnings("all")
@Entity
@Table(name = "rating")
@Data
@NoArgsConstructor
public class MovieRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id",nullable = false)
    private Long ratingId;
    @Column(nullable = false)
    private String comment;
    @Column(scale = 2,nullable = false)
    private Float rating;
    @Column(name = "review_date",nullable = false)
    private Date reviewDate;
    @Column(name = "user_email",nullable = false)
    private String userEmail;
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

}
