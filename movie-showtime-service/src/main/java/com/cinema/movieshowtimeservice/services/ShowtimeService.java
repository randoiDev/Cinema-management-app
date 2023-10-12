package com.cinema.movieshowtimeservice.services;

import com.cinema.movieshowtimeservice.dto.Response;
import com.cinema.movieshowtimeservice.dto.showtime.CreateReservationDto;
import com.cinema.movieshowtimeservice.dto.showtime.AddShowtimeDto;
import com.cinema.movieshowtimeservice.dto.showtime.CreateTicketDto;
import com.cinema.movieshowtimeservice.dto.showtime.ShowtimeDto;

import java.util.Collection;

public interface ShowtimeService {

    Response addShowtime(AddShowtimeDto showtimeDto);

    Response removeShowtime(Long showtimeId);

    Response acknowledgeShowtime(Long showtimeId);

    Collection<ShowtimeDto> getShowsByMovie(Integer page, Integer size, Long movieId);

    Collection<ShowtimeDto> getShowsByCinema(Integer page, Integer size, Long cinemaId);

    Collection<ShowtimeDto> getShowsByProjectionDate(Integer page, Integer size, Long showtimeDate);

    Response createReservation(CreateReservationDto createReservationDto);

    Response deleteReservation(Long reservationId);

    Response deleteReservationAdmin(String email);

    Response createTicket(CreateTicketDto ticketDto);

    Response deleteTicket(Long ticketId);

}
