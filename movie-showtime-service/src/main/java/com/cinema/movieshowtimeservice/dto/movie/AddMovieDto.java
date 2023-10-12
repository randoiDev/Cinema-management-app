package com.cinema.movieshowtimeservice.dto.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddMovieDto {

    @NotBlank(message = "Movie title is not valid!")
    private String title;
    @NotBlank(message = "Movie description is not valid!")
    private String description;
    @NotBlank(message = "Movie genre is not valid!")
    private String genre;
    @NotNull(message = "Movie release date is not valid!")
    private Long releaseDate;
    @NotNull(message = "Movie duration is not valid!")
    private Integer duration;

}
