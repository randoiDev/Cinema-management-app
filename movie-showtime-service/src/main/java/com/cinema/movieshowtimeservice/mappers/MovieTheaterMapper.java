package com.cinema.movieshowtimeservice.mappers;

import com.cinema.movieshowtimeservice.domain.Cinema;
import com.cinema.movieshowtimeservice.domain.MovieTheater;
import com.cinema.movieshowtimeservice.domain.MovieTheaterPK;
import com.cinema.movieshowtimeservice.domain.Seat;
import com.cinema.movieshowtimeservice.domain.enums.SeatType;
import com.cinema.movieshowtimeservice.dto.cinema.AddMovieTheaterDto;
import com.cinema.movieshowtimeservice.dto.cinema.MovieTheaterDto;
import org.springframework.stereotype.Component;

@Component
public class MovieTheaterMapper {

    public MovieTheater toEntity(Cinema cinema, AddMovieTheaterDto movieTheaterDto) {
        MovieTheater movieTheater = new MovieTheater();
        movieTheater.setMovieTheaterPK(new MovieTheaterPK(movieTheaterDto.getCinemaId(),movieTheaterDto.getMtNumber()));
        movieTheater.setCinema(cinema);
        return movieTheater;
    }

    public MovieTheaterDto toDto(MovieTheater movieTheater) {
        int economySeats = 0;
        int vipSeats = 0;
        for(Seat seat: movieTheater.getSeats()) {
            if(seat.getSeat().equals(SeatType.ECONOMY)) {
                economySeats++;
            } else if(seat.getSeat().equals(SeatType.VIP)) {
                vipSeats++;
            }
        }
        return MovieTheaterDto
                .builder()
                .mtNumber(movieTheater.getMovieTheaterPK().getMtNumber())
                .cinemaId(movieTheater.getMovieTheaterPK().getCinemaId())
                .economySeats(economySeats)
                .vipSeats(vipSeats)
                .build();
    }

}
