package com.cinema.movieshowtimeservice.mappers;

import com.cinema.movieshowtimeservice.domain.Movie;
import com.cinema.movieshowtimeservice.dto.movie.AddMovieDto;
import com.cinema.movieshowtimeservice.dto.movie.MovieDto;
import com.cinema.movieshowtimeservice.dto.movie.UpdateMovieDto;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {

    public Movie toEntityFromCreation(AddMovieDto movieDto) {
        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDuration(movieDto.getDuration());
        movie.setDescription(movieDto.getDescription());
        movie.setGenre(movieDto.getGenre());
        movie.setReleaseDate(new java.sql.Date(movieDto.getReleaseDate()));
        return movie;
    }

    public Movie toEntityFromUpdate(Movie movie, UpdateMovieDto movieDto) {
        if(movieDto.getTitle() != null && !movieDto.getTitle().isBlank()) {
            movie.setTitle(movieDto.getTitle());
        }
        if(movieDto.getDuration() != null && movieDto.getDuration() > 0) {
            movie.setDuration(movieDto.getDuration());
        }
        if(movieDto.getGenre() != null && !movieDto.getGenre().isBlank()) {
            movie.setGenre(movieDto.getGenre());
        }
        if(movieDto.getDescription() != null && !movieDto.getDescription().isBlank()) {
            movie.setDescription(movieDto.getDescription());
        }
        if(movieDto.getReleaseDate() != null && movieDto.getReleaseDate() > 0) {
            movie.setReleaseDate(new java.sql.Date(movieDto.getReleaseDate()));
        }
        return movie;
    }

    public MovieDto toDto(Movie movie) {
        return MovieDto
                .builder()
                .movieId(movie.getMovieId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .releaseDate(movie.getReleaseDate())
                .duration(movie.getDuration())
                .genre(movie.getGenre())
                .deleted(movie.isDeleted())
                .build();
    }

}
