package com.cinema.movieshowtimeservice.dto.cinema;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CinemaDto {

    private Long cinemaId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;

}
