package com.cinema.movieshowtimeservice.dto.movie;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateMovieDto {

    @NotNull(message = "Movie id is not valid!")
    private Long movieId;
    private String title;
    private String description;
    private String genre;
    private Long releaseDate;
    private Integer duration;

}
