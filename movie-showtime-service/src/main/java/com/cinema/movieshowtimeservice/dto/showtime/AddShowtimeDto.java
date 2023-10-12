package com.cinema.movieshowtimeservice.dto.showtime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddShowtimeDto {

    @NotNull(message = "Price for economy seats for this show is not valid!")
    private Integer economyPrice;
    @NotNull(message = "Price for vip seats for this show is not valid!")
    private Integer vipPrice;
    @NotNull(message = "Showtime movie id is not valid!")
    private Long movieId;
    @NotNull(message = "Showtime date is not valid!")
    private Long showtimeDate;
    @NotNull(message = "Showtime movie theater number is not valid!")
    private Integer mtNumber;
    @NotNull(message = "Showtime cinema id is not valid!")
    private Long cinemaId;

}
