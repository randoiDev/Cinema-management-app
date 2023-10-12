package com.cinema.movieshowtimeservice.dto.movie;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieRatingDto {

    private Long movieRatingId;
    private String comment;
    private Long reviewDate;
    private Float rating;
    private Long movieId;

}
