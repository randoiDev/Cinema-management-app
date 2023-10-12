package com.cinema.movieshowtimeservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("all")
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatPK {

    private MovieTheaterPK movieTheaterPK;
    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

}
