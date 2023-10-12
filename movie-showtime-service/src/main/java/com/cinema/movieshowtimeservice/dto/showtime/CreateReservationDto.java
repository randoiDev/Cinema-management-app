package com.cinema.movieshowtimeservice.dto.showtime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class CreateReservationDto {

    @NotNull(message = "Showtime id for this reservation is not valid!")
    private Long showtimeId;
    @NotNull(message = "Showtime reservation seat numbers are not valid!")
    private Collection<Integer> seatNumbers;

}
