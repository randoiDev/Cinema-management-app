package com.cinema.movieshowtimeservice.mappers;

import com.cinema.movieshowtimeservice.domain.Movie;
import com.cinema.movieshowtimeservice.domain.MovieTheater;
import com.cinema.movieshowtimeservice.domain.Showtime;
import com.cinema.movieshowtimeservice.dto.showtime.AddShowtimeDto;
import com.cinema.movieshowtimeservice.dto.showtime.ShowtimeDto;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class ShowtimeMapper {

    public Showtime toEntity(MovieTheater movieTheater, Movie movie, AddShowtimeDto showtimeDto) {
        Showtime showtime = new Showtime();
        showtime.setEconomyPrice(showtimeDto.getEconomyPrice());
        showtime.setVipPrice(showtimeDto.getVipPrice());
        showtime.setMovie(movie);
        showtime.setMovieTheater(movieTheater);
        showtime.setShowtimeDate(new Timestamp(showtimeDto.getShowtimeDate()));
        return showtime;
    }

    public ShowtimeDto toDto(Showtime showtime) {
        return ShowtimeDto
                .builder()
                .movieId(showtime.getMovie().getMovieId())
                .showtimeId(showtime.getShowtimeId())
                .economyPrice(showtime.getEconomyPrice())
                .vipPrice(showtime.getVipPrice())
                .cinemaId(showtime.getMovieTheater().getMovieTheaterPK().getCinemaId())
                .mtNumber(showtime.getMovieTheater().getMovieTheaterPK().getMtNumber())
                .showtimeDate(showtime.getShowtimeDate().getTime())
                .build();
    }

}
