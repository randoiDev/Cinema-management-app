package com.cinema.movieshowtimeservice.dto.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddMovieRatingDto {

    @NotBlank(message = "Rating comment is not valid!")
    private String comment;
    @NotNull(message = "Movie rating is not valid!")
    private Float rating;
    @NotNull(message = "Movie id is not valid!")
    private Long movieId;

}
