package com.cinema.movieshowtimeservice.dto.cinema;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieTheaterDto {

    private Long cinemaId;
    private Integer mtNumber;
    private Integer economySeats;
    private Integer vipSeats;

}
