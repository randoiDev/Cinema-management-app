package com.cinema.movieshowtimeservice.controllers;

import com.cinema.movieshowtimeservice.dto.Response;
import com.cinema.movieshowtimeservice.dto.cinema.*;
import com.cinema.movieshowtimeservice.services.CinemaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @<code>CinemaController</code> - Spring rest controller for accessing endpoints for manipulating cinema data.
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/cinema")
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaService cinemaService;

    /**
     * Endpoint for creating cinema record that requires <b>ADMIN</b> privilege to be accessed.
     * @see AddCinemaDto
     * @param cinemaDto
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
    public ResponseEntity<Response> addCinema(@Valid @RequestBody AddCinemaDto cinemaDto) {
        return ResponseEntity.ok(cinemaService.addCinema(cinemaDto));
    }

    /**
     * Endpoint for removing cinema record that requires <b>ADMIN</b> privilege to be accessed.
     * @param cinemaId
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping
    public ResponseEntity<Response> removeCinema(@RequestParam("cinemaId") Long cinemaId,
                                                 @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(cinemaService.removeCinema(cinemaId, authorization));
    }

    /**
     * Endpoint for updating cinema record that requires <b>ADMIN</b> privilege to be accessed.
     * @see UpdateCinemaDto
     * @param cinemaDto
     * @return
     */
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping
    public ResponseEntity<Response> updateCinemaInfo(@Valid @RequestBody UpdateCinemaDto cinemaDto) {
        return ResponseEntity.ok(cinemaService.updateCinemaInfo(cinemaDto));
    }

    /**
     * Endpoint that anyone logged in can access.
     * It is responsible for calling cinema service to retrieve a list of cinemas, the filtration is done
     * by matching provided search pattern with names,emails,addresses and cities of cinemas.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param searchPattern
     * @return <code>Collection</code> of cinemas that are in the system.
     */
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionCinemaResponse")
            }
    )
    @PreAuthorize("hasAnyAuthority('ADMIN','USER','CASHIER','MANAGER')")
    @GetMapping("/filter")
    public ResponseEntity<Iterable<CinemaDto>> filterCinemas(@RequestParam(defaultValue = "0") Integer page,
                                                             @RequestParam(defaultValue = "2") Integer size,
                                                             @RequestParam("searchPattern") String searchPattern) {
        return ResponseEntity.ok(cinemaService.filterCinemasBySearchPattern(page, size, searchPattern));
    }

    /**
     * Endpoint for retrieving info on cinema existence that requires <b>ADMIN</b> privilege to be accessed.
     * @param id
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @Operation(
            hidden = true
    )
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/cinema_id")
    public ResponseEntity<Response> getCinemaExistsById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(cinemaService.getCinemaExistsById(id));
    }

    /**
     * Endpoint for creating cinema movie theater record that requires <b>ADMIN</b> privilege to be accessed.
     * @see AddMovieTheaterDto
     * @param movieTheaterDto
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/movie_theater")
    public ResponseEntity<Response> createMovieTheater(@Valid @RequestBody AddMovieTheaterDto movieTheaterDto) {
        return ResponseEntity.ok(cinemaService.addMovieTheater(movieTheaterDto));
    }

    /**
     * Endpoint for removing cinema movie theater record that requires <b>ADMIN</b> privilege to be accessed.
     * @param cinemaId
     * @param mtNumber
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/movie_theater")
    public ResponseEntity<Response> deleteMovieTheater(@RequestParam("cinemaId") Long cinemaId, @RequestParam("mtNumber") Integer mtNumber) {
        return ResponseEntity.ok(cinemaService.removeMovieTheater(cinemaId, mtNumber));
    }

    /**
     * Endpoint that that requires <b>ADMIN</b> privilege to be accessed.
     * It is responsible for calling cinema service to retrieve a list of cinema movie theaters, the filtration is done
     * by matching provided id with cinemas id.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param cinemaId
     * @return <code>Collection</code> of cinema movie theaters that are in the system.
     */
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionTheaterResponse")
            }
    )
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/movie_theater")
    public ResponseEntity<Iterable<MovieTheaterDto>> getMovieTheatersByCinema(@RequestParam(defaultValue = "0") Integer page,
                                                                              @RequestParam(defaultValue = "2") Integer size,
                                                                              @RequestParam("cinemaId") Long cinemaId) {
        return ResponseEntity.ok(cinemaService.getMovieTheatersByCinema(page, size, cinemaId));
    }

}
