package com.cinema.movieshowtimeservice.dto.cinema;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddMovieTheaterDto {

    @NotNull(message = "Cinema id is not valid!")
    private Long cinemaId;
    @NotNull(message = "Movie theater number is not valid!")
    private Integer mtNumber;
    @NotNull(message = "Economy seats number is not valid!")
    private Integer economySeats;
    @NotNull(message = "VIP seats number is not valid!")
    private Integer vipSeats;

}
