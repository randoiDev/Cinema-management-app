package com.cinema.movieshowtimeservice.services;

import com.cinema.movieshowtimeservice.dto.Response;
import com.cinema.movieshowtimeservice.dto.movie.*;

import java.sql.Date;
import java.util.Collection;

public interface MovieService {

    Response addMovie(AddMovieDto movieDto);

    Response removeMovie(Long movieId);

    Response updateMovieInfo(UpdateMovieDto movieDto);

    Collection<MovieDto> getMoviesByReleaseYear(Integer page, Integer size, Integer year);

    Collection<MovieDto> filterMoviesBySearchPattern(Integer page, Integer size, String searchPattern);

    Response addMovieRating(AddMovieRatingDto movieRatingDto);

    Response removeMovieRating(Long movieRatingId);

    Collection<MovieRatingDto> getMovieRatingsByMovie(Integer page, Integer size, Long movieId);

}
