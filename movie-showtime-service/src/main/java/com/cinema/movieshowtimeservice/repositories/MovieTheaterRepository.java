package com.cinema.movieshowtimeservice.repositories;

import com.cinema.movieshowtimeservice.domain.MovieTheater;
import com.cinema.movieshowtimeservice.domain.MovieTheaterPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieTheaterRepository extends JpaRepository<MovieTheater, Long> {

    @Query("select mt from MovieTheater mt where mt.cinema.cinemaId= :cinemaId")
    Page<MovieTheater> getMovieTheaterByCinema(Pageable page, @Param("cinemaId") Long cinemaId);

    Optional<MovieTheater> getMovieTheaterByMovieTheaterPK(MovieTheaterPK movieTheaterPK);
}
