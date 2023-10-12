package com.cinema.movieshowtimeservice.dto.movie;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class MovieDto {

    private Long movieId;
    private String title;
    private String description;
    private String genre;
    private Date releaseDate;
    private Integer duration;
    private boolean deleted;

}
