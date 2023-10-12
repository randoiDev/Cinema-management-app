package com.cinema.movieshowtimeservice.repositories;

import com.cinema.movieshowtimeservice.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> getMovieByMovieId(Long movieId);

    @Query("select m from Movie m where year(m.releaseDate) = :year")
    Page<Movie> getMoviesByReleaseYear(Pageable page, @RequestParam("year") Integer year);

    @Query("select m from Movie m where m.title like %:pattern% " +
            "or m.description  like %:pattern% " +
            "or m.genre  like %:pattern%")
    Page<Movie> filterMoviesBySearchPattern(Pageable page, @Param("pattern") String searchPattern);

}
