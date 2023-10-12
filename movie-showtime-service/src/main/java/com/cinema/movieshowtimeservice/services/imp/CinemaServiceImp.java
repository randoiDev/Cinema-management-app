package com.cinema.movieshowtimeservice.services.imp;

import com.cinema.movieshowtimeservice.domain.*;
import com.cinema.movieshowtimeservice.domain.enums.SeatType;
import com.cinema.movieshowtimeservice.domain.enums.ShowtimeState;
import com.cinema.movieshowtimeservice.dto.Response;
import com.cinema.movieshowtimeservice.dto.cinema.*;
import com.cinema.movieshowtimeservice.exceptions.IllegalStateExceptionExtension;
import com.cinema.movieshowtimeservice.exceptions.RecordNotFoundException;
import com.cinema.movieshowtimeservice.mappers.CinemaMapper;
import com.cinema.movieshowtimeservice.mappers.MovieTheaterMapper;
import com.cinema.movieshowtimeservice.repositories.CinemaRepository;
import com.cinema.movieshowtimeservice.repositories.MovieTheaterRepository;
import com.cinema.movieshowtimeservice.services.CinemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @<code>CinemaServiceImp</code> - Class that has methods that are responsible for invoking business logic for a specific endpoint call.
 */


@SuppressWarnings("all")
@Service
@RequiredArgsConstructor
public class CinemaServiceImp implements CinemaService {

    private final CinemaRepository cinemaRepository;
    private final CinemaMapper cinemaMapper;
    private final MovieTheaterRepository movieTheaterRepository;
    private final MovieTheaterMapper movieTheaterMapper;
    private final RestTemplate restTemplate;
    @Value("${user-service.host}")
    private String userHostname;

    /**
     * Method that is responsible for adding cinemas to database.
     * Pretty straightforward..
     * @see CinemaMapper
     * @see AddCinemaDto
     * @param cinemaDto
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response addCinema(AddCinemaDto cinemaDto) {
        cinemaRepository.save(cinemaMapper.toEntityFromCreation(cinemaDto));
        return new Response("Cinema '" + cinemaDto.getName() + "' added successfully!");
    }

    /**
     * This method is responsible for removing cinema from database.
     * First is performed check whether the cinema exists for the provided cinema id, and
     * if does, then the iteration through cinema's shows happen to check if every single
     * one of them is finished because we dont want to delete cinema if shows are in progress
     * and at last we send a delete request to user service to delete every employee from
     * the specified cinema before it is deleted so there wont be hanging employees in the
     * other service database.
     * @exception RecordNotFoundException
     * @exception IllegalStateExceptionExtension
     * @param cinemaId
     * @param authorization
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response removeCinema(Long cinemaId, String authorization) {
        Cinema cinema = cinemaRepository.getCinemaByCinemaId(cinemaId)
                .orElseThrow(() -> new RecordNotFoundException(Cinema.class.getSimpleName(), cinemaId + ""));
        for (MovieTheater movieTheater : cinema.getMovieTheaters()) {
            for (Showtime showtime : movieTheater.getShows()) {
                if (!showtime.getState().equals(ShowtimeState.FINISHED)) {
                    throw new IllegalStateExceptionExtension("Cinema '" + cinema.getName() + "' cannot be removed until all its projections are finished!");
                }
            }
        }
        String jwt = authorization.split(" ")[1];
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        HttpEntity httpEntity = new HttpEntity(null, httpHeaders);
        String uri = "http://" + userHostname + ":8080/api/employee/cinema?id=" + cinemaId;
        restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Response.class);
        cinemaRepository.delete(cinema);
        return new Response("Cinema '" + cinema.getName() + "' removed successfully!");
    }

    /**
     * This method is responsible for updating cinema record in database.
     * First is performed check whether the provided cinema exists and if
     * it does, then it is updated.
     * @exception RecordNotFoundException
     * @see UpdateCinemaDto
     * @param cinemaDto
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response updateCinemaInfo(UpdateCinemaDto cinemaDto) {
        Cinema cinema = cinemaRepository.getCinemaByCinemaId(cinemaDto.getCinemaId())
                .orElseThrow(() -> new RecordNotFoundException(Cinema.class.getSimpleName(), cinemaDto.getCinemaId() + ""));
        cinemaRepository.save(cinemaMapper.toEntityFromUpdate(cinema, cinemaDto));
        return new Response("Info for cinema '" + cinema.getName() + "' is successfully updated!");
    }

    /**
     * This method retrieves list of cinemas which are filtered by specified search pattern page by page.
     * Search patten here is matched with names,addresses,cities and emails of movies.
     * @see CinemaRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param searchPattern
     * @return <code>Collection</code> of cinemas.
     */
    @Override
    public Collection<CinemaDto> filterCinemasBySearchPattern(Integer page, Integer size, String searchPattern) {
        Iterable<Cinema> collection = cinemaRepository.filterCinemasBySearchPattern(PageRequest.of(page, size), searchPattern);
        Iterator<Cinema> iterator = collection.iterator();
        List<CinemaDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(cinemaMapper.toDto(iterator.next()));
        }
        return newCollection;
    }

    /**
     * This method just checks whether a cinema exists in the database for the
     * specified cinema id.
     * @exception RecordNotFoundException
     * @param id
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response getCinemaExistsById(Long id) {
        Cinema cinema = cinemaRepository.getCinemaByCinemaId(id)
                .orElseThrow(() -> new RecordNotFoundException(Cinema.class.getSimpleName(),id + ""));
        return new Response("Cinema with id:" + id + " exists!");
    }

    /**
     * This method is responsible for adding movie theaters to the database.
     * First we check if a cinema exists for the specified id, if it does then
     * we create movie theater with its mapper and add it to cinemas's movie
     * theater list.After that we create economy and vip seats based on
     * their specified number in provided DTO object and add them to movie
     * theater's list of seats.In the end we save cinema because persistance
     * will be propagated to a newly added movie theater and newly added seats.
     * @exception RecordNotFoundException
     * @see MovieTheaterMapper
     * @see AddMovieTheaterDto
     * @param movieTheaterDto
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response addMovieTheater(AddMovieTheaterDto movieTheaterDto) {
        Cinema cinema = cinemaRepository.getCinemaByCinemaId(movieTheaterDto.getCinemaId())
                .orElseThrow(() -> new RecordNotFoundException(Cinema.class.getSimpleName(), movieTheaterDto.getCinemaId() + ""));
        MovieTheater movieTheater = movieTheaterMapper.toEntity(cinema, movieTheaterDto);
        cinema.getMovieTheaters().add(movieTheater);
        int i = 1;
        for (; i < movieTheaterDto.getEconomySeats() + 1; i++) {
            Seat seat = new Seat();
            seat.setSeatPK(new SeatPK(movieTheater.getMovieTheaterPK(), i));
            seat.setSeat(SeatType.ECONOMY);
            movieTheater.getSeats().add(seat);
            seat.setMovieTheater(movieTheater);
        }
        for (; i < movieTheaterDto.getEconomySeats() + movieTheaterDto.getVipSeats() + 1; i++) {
            Seat seat = new Seat();
            seat.setSeatPK(new SeatPK(movieTheater.getMovieTheaterPK(), i));
            seat.setSeat(SeatType.VIP);
            movieTheater.getSeats().add(seat);
            seat.setMovieTheater(movieTheater);
        }
        cinemaRepository.save(cinema);
        return new Response("Movie theater number " + movieTheater.getMovieTheaterPK().getMtNumber() + " for cinema '" + cinema.getName() + "' successfully added!");
    }

    /**
     * This method is responsible for removing cinema movie theater from database.
     * First we check if provided mt exists, if it does, then we check if all shows
     * correlated to that mt are finished and if they are, we remove mt from cinema's
     * list of mt's and we delete specified movie theater that will also delete all
     * of its seats from database because of the propagation of remove action.
     * @exception RecordNotFoundException
     * @exception IllegalStateExceptionExtension
     * @param cinemaId
     * @param mtNumber
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response removeMovieTheater(Long cinemaId, Integer mtNumber) {
        MovieTheater movieTheater = movieTheaterRepository.getMovieTheaterByMovieTheaterPK(new MovieTheaterPK(cinemaId, mtNumber))
                .orElseThrow(() -> new RecordNotFoundException(MovieTheater.class.getSimpleName(), cinemaId + " " + mtNumber));
        for (Showtime showtime : movieTheater.getShows()) {
            if (!showtime.getState().equals(ShowtimeState.FINISHED)) {
                throw new IllegalStateExceptionExtension("Movie theater number " + movieTheater.getMovieTheaterPK().getMtNumber() + " at cinema '" + movieTheater.getCinema().getName() + "' cannot be removed until all its projections are finished!");
            }
        }
        Cinema cinema = movieTheater.getCinema();
        cinema.getMovieTheaters().remove(movieTheater);
        movieTheaterRepository.delete(movieTheater);
        cinemaRepository.save(cinema);
        return new Response("Movie theater number " + movieTheater.getMovieTheaterPK().getMtNumber() + " for cinema '" + cinema.getName() + "' successfully removed!");
    }

    /**
     * This method retrieves list of cinema's movie theaters which are filtered by specified search pattern page by page.
     * Provided cinema's id here is matched with movie theater's cinema id in the database.
     * @see CinemaRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param cinemaId
     * @return <code>Collection</code> of cinema's movie theaters.
     */
    @Override
    public Collection<MovieTheaterDto> getMovieTheatersByCinema(Integer page, Integer size, Long cinemaId) {
        Iterable<MovieTheater> collection = movieTheaterRepository.getMovieTheaterByCinema(PageRequest.of(page, size), cinemaId);
        Iterator<MovieTheater> iterator = collection.iterator();
        List<MovieTheaterDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(movieTheaterMapper.toDto(iterator.next()));
        }
        return newCollection;
    }


}
