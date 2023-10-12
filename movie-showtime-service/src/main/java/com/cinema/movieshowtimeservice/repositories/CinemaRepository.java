package com.cinema.movieshowtimeservice.repositories;

import com.cinema.movieshowtimeservice.domain.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CinemaRepository extends JpaRepository<Cinema,Long> {

    Optional<Cinema> getCinemaByCinemaId(Long cinemaId);

    @Query("select c from Cinema c where c.name like %:pattern% " +
            "or c.email like %:pattern% " +
            "or c.address like %:pattern% " +
            "or c.city like %:pattern%")
    Page<Cinema> filterCinemasBySearchPattern(Pageable pageable, @Param("pattern") String searchPattern);

}
