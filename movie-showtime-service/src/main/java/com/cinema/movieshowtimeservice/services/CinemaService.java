package com.cinema.movieshowtimeservice.services;

import com.cinema.movieshowtimeservice.dto.Response;
import com.cinema.movieshowtimeservice.dto.cinema.*;

import java.util.Collection;

public interface CinemaService {

    Response addCinema(AddCinemaDto cinemaDto);

    Response removeCinema(Long cinemaId, String authorization);

    Response updateCinemaInfo(UpdateCinemaDto cinemaDto);

    Collection<CinemaDto> filterCinemasBySearchPattern(Integer page, Integer size, String searchPattern);

    Response getCinemaExistsById(Long id);

    Response addMovieTheater(AddMovieTheaterDto movieTheaterDto);

    Response removeMovieTheater(Long cinemaId, Integer mtNumber);

    Collection<MovieTheaterDto> getMovieTheatersByCinema(Integer page, Integer size, Long cinemaId);

}
