package com.cinema.movieshowtimeservice.controllers;

import com.cinema.movieshowtimeservice.dto.Response;
import com.cinema.movieshowtimeservice.dto.movie.*;
import com.cinema.movieshowtimeservice.services.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @<code>MovieController</code> - Spring rest controller for accessing endpoints for manipulating movie data.
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    /**
     * Endpoint for creating movie record that requires <b>ADMIN</b> privilege to be accessed.
     * @see AddMovieDto
     * @param movieDto
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> addMovie(@Valid @RequestBody AddMovieDto movieDto) {
        return ResponseEntity.ok(movieService.addMovie(movieDto));
    }

    /**
     * Endpoint for removing movie record that requires <b>ADMIN</b> privilege to be accessed.
     * @param movieId
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> removeMovie(@RequestParam("movieId") Long movieId) {
        return ResponseEntity.ok(movieService.removeMovie(movieId));
    }

    /**
     * Endpoint for updating movie record that requires <b>ADMIN</b> privilege to be accessed.
     * @see UpdateMovieDto
     * @param movieDto
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> updateMovieInfo(@Valid @RequestBody UpdateMovieDto movieDto) {
        return ResponseEntity.ok(movieService.updateMovieInfo(movieDto));
    }

    /**
     * Endpoint that anyone logged in can access.
     * It is responsible for calling movie service to retrieve a list of movies, the filtration is done
     * by matching provided search pattern with titles,descriptions and genres of movies.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param searchPattern
     * @return <code>Collection</code> of movies that are in the system.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','CASHIER','MANAGER')")
    @GetMapping("/search_pattern")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionMovieResponse")
            }
    )
    public ResponseEntity<Iterable<MovieDto>> filterMoviesBySearchPattern(@RequestParam(defaultValue = "0") Integer page,
                                                                          @RequestParam(defaultValue = "2") Integer size,
                                                                          @RequestParam("searchPattern") String searchPattern) {
        return ResponseEntity.ok(movieService.filterMoviesBySearchPattern(page, size, searchPattern));
    }

    /**
     * Endpoint that anyone logged in can access.
     * It is responsible for calling movie service to retrieve a list of movies, the filtration is done
     * by matching provided release year with release year of movies.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param year
     * @return <code>Collection</code> of movies that are in the system.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','CASHIER','MANAGER')")
    @GetMapping("/release_year")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionMovieResponse")
            }
    )
    public ResponseEntity<Iterable<MovieDto>> getMoviesByReleaseYear(@RequestParam(defaultValue = "0") Integer page,
                                                                     @RequestParam(defaultValue = "2") Integer size,
                                                                     @RequestParam("releaseYear") Integer year) {
        return ResponseEntity.ok(movieService.getMoviesByReleaseYear(page, size, year));
    }

    /**
     * Endpoint for creating movie rating record that requires <b>USER</b> privilege to be accessed.
     * @see AddMovieRatingDto
     * @param addMovieRatingDto
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/rating")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> addMovieRating(@Valid @RequestBody AddMovieRatingDto addMovieRatingDto) {
        return ResponseEntity.ok(movieService.addMovieRating(addMovieRatingDto));
    }

    /**
     * Endpoint for removing movie rating record that requires <b>USER</b> privilege to be accessed.
     * @param movieRatingId
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAnyAuthority('USER')")
    @DeleteMapping("/rating")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> removeMovieRating(@RequestParam("movieRatingId") Long movieRatingId) {
        return ResponseEntity.ok(movieService.removeMovieRating(movieRatingId));
    }

    /**
     * Endpoint that requires <b>USER</b> privilege so it can be accessed.
     * It is responsible for calling movie service to retrieve a list of movies rating, the filtration is done
     * by matching provided movie id with movie id of a movie rating.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param movieId
     * @return <code>Collection</code> of movie ratings that are in the system.
     */
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping("/rating")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionMovieRatingResponse")
            }
    )
    public ResponseEntity<Iterable<MovieRatingDto>> getMovieRatingsByMovie(@RequestParam(defaultValue = "0") Integer page,
                                                                           @RequestParam(defaultValue = "2") Integer size,
                                                                           @RequestParam("movieId") Long movieId) {
        return ResponseEntity.ok(movieService.getMovieRatingsByMovie(page, size, movieId));
    }

}
