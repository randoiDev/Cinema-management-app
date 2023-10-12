package com.cinema.movieshowtimeservice.mappers;

import com.cinema.movieshowtimeservice.domain.Movie;
import com.cinema.movieshowtimeservice.domain.MovieRating;
import com.cinema.movieshowtimeservice.dto.movie.AddMovieRatingDto;
import com.cinema.movieshowtimeservice.dto.movie.MovieRatingDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class MovieRatingMapper {

    public MovieRating toEntity(AddMovieRatingDto movieRatingDto, Movie movie) {
        MovieRating movieRating = new MovieRating();
        movieRating.setMovie(movie);
        movieRating.setRating(movieRatingDto.getRating());
        movieRating.setComment(movieRatingDto.getComment());
        movieRating.setReviewDate(new Date(System.currentTimeMillis()));
        movieRating.setUserEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return movieRating;
    }

    public MovieRatingDto toDto(MovieRating movieRating) {
        return MovieRatingDto
                .builder()
                .movieRatingId(movieRating.getRatingId())
                .movieId(movieRating.getMovie().getMovieId())
                .comment(movieRating.getComment())
                .reviewDate(movieRating.getReviewDate().getTime())
                .rating(movieRating.getRating())
                .build();
    }

}
