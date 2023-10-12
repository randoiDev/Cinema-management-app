package com.cinema.movieshowtimeservice.repositories;

import com.cinema.movieshowtimeservice.domain.Seat;
import com.cinema.movieshowtimeservice.domain.SeatPK;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {

    Optional<Seat> getSeatBySeatPK(SeatPK seatPK);

}
