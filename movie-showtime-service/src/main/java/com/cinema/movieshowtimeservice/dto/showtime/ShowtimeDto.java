package com.cinema.movieshowtimeservice.dto.showtime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShowtimeDto {

    private Long showtimeId;
    private Integer economyPrice;
    private Integer vipPrice;
    private Long showtimeDate;
    private Long movieId;
    private Long cinemaId;
    private Integer mtNumber;

}
