package com.cinema.movieshowtimeservice.repositories;

import com.cinema.movieshowtimeservice.domain.Showtime;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.Optional;

@SuppressWarnings("all")
@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime,Long> {

    Optional<Showtime> getShowtimeByShowtimeId(Long showtimeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Showtime s where s.showtimeId = :showtimeId")
    Optional<Showtime> getShowtimeByShowtimeIdLock(@RequestParam("showtimeId") Long showtimeId);

    @Query("select s from Showtime s where s.movie.movieId = :movieId")
    Page<Showtime> getShowtimesByMovie(Pageable page, Long movieId);

    @Query("select s from Showtime s where s.movieTheater.cinema.cinemaId = :cinemaId")
    Page<Showtime> getShowtimesByCinema(Pageable page, @Param("cinemaId") Long cinemaId);

    @Query(value = "SELECT * FROM showtime s WHERE DATE(s.showtime_date) = :date", nativeQuery = true)
    Page<Showtime> getShowtimesByShowtimeDate(Pageable page, @RequestParam("date") Date date);

}
