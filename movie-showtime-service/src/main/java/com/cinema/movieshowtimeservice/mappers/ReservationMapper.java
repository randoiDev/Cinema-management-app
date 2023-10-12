package com.cinema.movieshowtimeservice.mappers;

import com.cinema.movieshowtimeservice.domain.Reservation;
import com.cinema.movieshowtimeservice.domain.Showtime;
import com.cinema.movieshowtimeservice.dto.showtime.CreateReservationDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ReservationMapper {

    public Reservation toEntity(Showtime showtime, CreateReservationDto createReservationDto) {
        Reservation reservation = new Reservation();
        reservation.setReservationDate(new Timestamp(System.currentTimeMillis()));
        reservation.setShow(showtime);
        reservation.setUserEmail(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        return reservation;
    }
}
