package com.cinema.movieshowtimeservice.repositories;

import com.cinema.movieshowtimeservice.domain.MovieRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRating, Long> {

    Optional<MovieRating> getMovieRatingsByUserEmailAndRatingId(String email, Long movieRatingId);

    @Query("select mr from MovieRating mr where mr.movie.movieId = :movieId")
    Page<MovieRating> getMovieRatingsByMovie(Pageable pageable, Long movieId);

}
