package com.cinema.movieshowtimeservice.controllers;

import com.cinema.movieshowtimeservice.dto.Response;
import com.cinema.movieshowtimeservice.dto.showtime.AddShowtimeDto;
import com.cinema.movieshowtimeservice.dto.showtime.CreateReservationDto;
import com.cinema.movieshowtimeservice.dto.showtime.CreateTicketDto;
import com.cinema.movieshowtimeservice.dto.showtime.ShowtimeDto;
import com.cinema.movieshowtimeservice.services.ShowtimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @<code>ShowtimeController</code> - Spring rest controller for accessing endpoints for manipulating showtime data.
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/showtime")
@RequiredArgsConstructor
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    /**
     * Endpoint for creating movie showtime that requires <b>ADMIN</b> or <b>MANAGER</b> privilege to be accessed.
     * @see AddShowtimeDto
     * @param showtimeDto
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN')")
    @PostMapping
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> addShowtime(@Valid @RequestBody AddShowtimeDto showtimeDto) {
        return ResponseEntity.ok(showtimeService.addShowtime(showtimeDto));
    }

    /**
     * Endpoint for removing movie showtime that requires <b>ADMIN</b> privilege to be accessed.
     * @param showtimeId
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @DeleteMapping
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> removeShowtime(@RequestParam("showtimeId") Long showtimeId) {
        return ResponseEntity.ok(showtimeService.removeShowtime(showtimeId));
    }

    /**
     * Endpoint for updating movie showtime that requires <b>ADMIN</b> privilege to be accessed.
     * @param showtimeId
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PutMapping
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> acknowledgeShowtime(@RequestParam("showtimeId") Long showtimeId) {
        return ResponseEntity.ok(showtimeService.acknowledgeShowtime(showtimeId));
    }

    /**
     * Endpoint that anyone logged in can access.
     * It is responsible for calling showtime service to retrieve a list of shows, the filtration is done
     * by matching provided movie id with movie id of a show.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param movieId
     * @return <code>Collection</code> of shows that are in the system.
     */
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN','USER','CASHIER')")
    @GetMapping("/movie")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionShowtimeResponse")
            }
    )
    ResponseEntity<Iterable<ShowtimeDto>> getShowsByMovie(@RequestParam(defaultValue = "0") Integer page,
                                                          @RequestParam(defaultValue = "2") Integer size,
                                                          @RequestParam("movieId") Long movieId) {
        return ResponseEntity.ok(showtimeService.getShowsByMovie(page, size, movieId));
    }

    /**
     * Endpoint that anyone logged in can access.
     * It is responsible for calling showtime service to retrieve a list of shows, the filtration is done
     * by matching provided cinema id with cinema id of a show.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param cinemaId
     * @return <code>Collection</code> of shows that are in the system.
     */
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN','USER','CASHIER')")
    @GetMapping("/cinema")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionShowtimeResponse")
            }
    )
    ResponseEntity<Iterable<ShowtimeDto>> getShowsByCinema(@RequestParam(defaultValue = "0") Integer page,
                                                           @RequestParam(defaultValue = "2") Integer size,
                                                           @RequestParam("cinemaId") Long cinemaId) {
        return ResponseEntity.ok(showtimeService.getShowsByCinema(page, size, cinemaId));
    }

    /**
     * Endpoint that anyone logged in can access.
     * It is responsible for calling showtime service to retrieve a list of shows, the filtration is done
     * by matching provided show's date with showtime date of a show.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param showtimeDate
     * @return <code>Collection</code> of shows that are in the system.
     */
    @PreAuthorize("hasAnyAuthority('MANAGER','ADMIN','USER','CASHIER')")
    @GetMapping("/showtime_date")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionShowtimeResponse")
            }
    )
    ResponseEntity<Iterable<ShowtimeDto>> getShowsByProjectionDate(@RequestParam(defaultValue = "0") Integer page,
                                                                   @RequestParam(defaultValue = "2") Integer size,
                                                                   @RequestParam("showtimeDate") Long showtimeDate) {
        return ResponseEntity.ok(showtimeService.getShowsByProjectionDate(page, size, showtimeDate));
    }

    /**
     * Endpoint for creating showtime reservation record that requires <b>USER</b> privilege to be accessed.
     * @see CreateReservationDto
     * @param reservationDto
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAnyAuthority('USER')")
    @PostMapping("/reservation")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> createReservation(@Valid @RequestBody CreateReservationDto reservationDto) {
        return ResponseEntity.ok(showtimeService.createReservation(reservationDto));
    }

    /**
     * Endpoint for removing showtime reservation record that requires <b>USER</b> privilege to be accessed.
     * @param reservationId
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAnyAuthority('USER')")
    @DeleteMapping("/reservation")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> deleteReservation(@RequestParam("reservationId") Long reservationId) {
        return ResponseEntity.ok(showtimeService.deleteReservation(reservationId));
    }

    /**
     * Endpoint for removing showtime reservation record that requires <b>CASHIER</b> privilege to be accessed.
     * @param email
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/reservation_admin")
    public ResponseEntity<Response> deleteReservationAdmin(@RequestParam("email") String email) {
        return ResponseEntity.ok(showtimeService.deleteReservationAdmin(email));
    }

    /**
     * Endpoint for creating showtime ticket record that requires <b>CASHIER</b> privilege to be accessed.
     * @see CreateTicketDto
     * @param ticketDto
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAnyAuthority('CASHIER')")
    @PostMapping("/ticket")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> createTicket(@Valid @RequestBody CreateTicketDto ticketDto) {
        return ResponseEntity.ok(showtimeService.createTicket(ticketDto));
    }

    /**
     * Endpoint for removing showtime ticket record that requires <b>CASHIER</b> privilege to be accessed.
     * @param ticketId
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAnyAuthority('CASHIER')")
    @DeleteMapping("/ticket")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> deleteTicket(@RequestParam("ticketId") Long ticketId) {
        return ResponseEntity.ok(showtimeService.deleteTicket(ticketId));
    }

}
