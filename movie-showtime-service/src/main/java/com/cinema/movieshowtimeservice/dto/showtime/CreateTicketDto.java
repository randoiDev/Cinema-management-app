package com.cinema.movieshowtimeservice.dto.showtime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTicketDto {

    @NotNull(message = "Showtime id for this ticket is not valid!")
    private Long showtimeId;
    @NotNull(message = "Seat number for this ticket is not valid!")
    private Integer seatNumber;

}
