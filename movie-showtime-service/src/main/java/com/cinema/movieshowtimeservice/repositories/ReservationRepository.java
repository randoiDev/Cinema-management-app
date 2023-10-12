package com.cinema.movieshowtimeservice.repositories;

import com.cinema.movieshowtimeservice.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    Optional<Reservation> getReservationByUserEmailAndReservationId(String userEmail,Long reservationId);

    List<Reservation> getReservationsByUserEmail(String email);



}
