package com.cinema.movieshowtimeservice.services.imp;

import com.cinema.movieshowtimeservice.domain.*;
import com.cinema.movieshowtimeservice.domain.enums.ShowtimeState;
import com.cinema.movieshowtimeservice.dto.NotificationDto;
import com.cinema.movieshowtimeservice.dto.Response;
import com.cinema.movieshowtimeservice.dto.enums.NotificationType;
import com.cinema.movieshowtimeservice.dto.showtime.AddShowtimeDto;
import com.cinema.movieshowtimeservice.dto.showtime.CreateReservationDto;
import com.cinema.movieshowtimeservice.dto.showtime.CreateTicketDto;
import com.cinema.movieshowtimeservice.dto.showtime.ShowtimeDto;
import com.cinema.movieshowtimeservice.exceptions.IllegalStateExceptionExtension;
import com.cinema.movieshowtimeservice.exceptions.RecordNotFoundException;
import com.cinema.movieshowtimeservice.mappers.ReservationMapper;
import com.cinema.movieshowtimeservice.mappers.ShowtimeMapper;
import com.cinema.movieshowtimeservice.mappers.TicketMapper;
import com.cinema.movieshowtimeservice.repositories.*;
import com.cinema.movieshowtimeservice.services.AMQPService;
import com.cinema.movieshowtimeservice.services.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @<code>ShowtimeServiceImp</code> - Class that has methods that are responsible for invoking business logic for a specific endpoint call.
 */

@SuppressWarnings("all")
@Service
@RequiredArgsConstructor
public class ShowtimeServiceImp implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final ShowtimeMapper showtimeMapper;
    private final MovieRepository movieRepository;
    private final MovieTheaterRepository movieTheaterRepository;
    private final SeatRepository seatRepository;
    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TaskScheduler taskScheduler;
    private final AMQPService amqpService;

    /**
     * Method that removes sold ticket records from database
     * every day at midnight for every show that is finished.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeTicketsScheduled() {
        List<Ticket> tickets = ticketRepository.findAll();
        for(Ticket ticket: tickets) {
            if(ticket.getShow().getState().equals(ShowtimeState.FINISHED)) {
                ticketRepository.delete(ticket);
            }
        }
    }

    /**
     * Method that is responsible for adding showtime record to database.
     * First we check if showtime date is 2 days ahead from now, if is then
     * we try to retrieve both movie and movie theater that are specified in
     * the sent request DTO object, also check if movie is soft deleted is
     * implied.Then we update everything based on cascade types that were set,
     * and finally we save newly created showtime if everything is okay.
     * @exception RecordNotFoundException
     * @exception IllegalStateExceptionExtension
     * @see ShowtimeMapper
     * @see AddShowtimeDto
     * @param showtimeDto
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Response addShowtime(AddShowtimeDto showtimeDto) {
        long twoDaysMillis = 2 * 24 * 60 * 60 * 1000;
        if (showtimeDto.getShowtimeDate() - System.currentTimeMillis() < twoDaysMillis)
            throw new IllegalStateExceptionExtension("The projection date must be set 2 days in advance!");
        Movie movie = movieRepository.getMovieByMovieId(showtimeDto.getMovieId())
                .orElseThrow(() -> new RecordNotFoundException(Movie.class.getSimpleName(), showtimeDto.getMovieId() + ""));
        if (movie.isDeleted())
            throw new IllegalStateExceptionExtension("Cannot create showtime because the movie " + movie.getTitle() + " is deleted!");
        MovieTheater movieTheater = movieTheaterRepository
                .getMovieTheaterByMovieTheaterPK(new MovieTheaterPK(showtimeDto.getCinemaId(), showtimeDto.getMtNumber()))
                .orElseThrow(() -> new RecordNotFoundException(MovieTheater.class.getSimpleName(), showtimeDto.getMtNumber() + ""));
        Showtime showtime = showtimeMapper.toEntity(movieTheater, movie, showtimeDto);
        movie.getShows().add(showtime);
        movieTheater.getShows().add(showtime);
        //This will also save newly created showtime because of the specified propagation
        movieTheaterRepository.save(movieTheater);
        movieRepository.save(movie);

        return new Response("Showtime for movie " + movie.getTitle() + " added successfully!");
    }

    /**
     * This method is responsible for removing showtime from database.
     * First is provided search for the existing showtime as we usually
     * did in many methods before.Then we can only delete showtime if it
     * is in <b>CREATION</b> or <b>FINISHED</b> state.If it is in one of
     * these then we update its corresponding movie entity by updating list
     * of showtimes, we also update its movie theater's list of showtimes
     * and finally we remove it.Its removal will also deleted corresponding
     * tickets for that show (no need for deleting reservations since they
     * are being deleted by a task scheduler at 5 hour before it projection
     * time).
     * @exception IllegalStateExceptionExtension
     * @exception RecordNotFoundException
     * @param showtimeId
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response removeShowtime(Long showtimeId) {
        Showtime showtime = showtimeRepository.getShowtimeByShowtimeId(showtimeId)
                .orElseThrow(() -> new RecordNotFoundException(Showtime.class.getSimpleName(), showtimeId + ""));
        if (showtime.getState().equals(ShowtimeState.CREATION) || showtime.getState().equals(ShowtimeState.FINISHED)) {
            Movie movie = showtime.getMovie();
            MovieTheater movieTheater = showtime.getMovieTheater();
            movie.getShows().remove(showtime);
            movieRepository.save(movie);
            movieTheater.getShows().remove(showtime);
            movieTheaterRepository.save(movieTheater);
            showtimeRepository.delete(showtime);
        } else {
            throw new IllegalStateExceptionExtension("Showtime for movie " + showtime.getMovie().getTitle() + " can't be removed until it's projection is finished!");
        }
        return new Response("Showtime for movie " + showtime.getMovie().getTitle() + " removed successfully!");
    }

    /**
     * This method is responsible for updating showtime state.
     * This is done when we are sure that we want this projection
     * to be held at some point.We first check if showtime is in
     * <b>CREATION</b> state and if it is we can confirm its projection
     * date.The actions of task scheduler are described above their
     * definition in code of this method below.
     * @exception RecordNotFoundException
     * @exception IllegalStateExceptionExtension
     * @param showtimeId
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response acknowledgeShowtime(Long showtimeId) {
        Showtime showtime = showtimeRepository.getShowtimeByShowtimeId(showtimeId)
                .orElseThrow(() -> new RecordNotFoundException(Showtime.class.getSimpleName(), showtimeId + ""));
        if (showtime.getState().equals(ShowtimeState.CREATION)) {
            showtime.setState(ShowtimeState.SCHEDULED);
            showtimeRepository.save(showtime);
        } else {
            throw new IllegalStateException("Showtime for movie " + showtime.getMovie().getTitle() + " is already confirmed!");
        }

        long fiveHours = 5 * 60 * 60 * 1000;

        /**
         * Here we set a scheduler that is going to delete all the reservations
         * for the showtime at fifth hour before its projection.Everything is being
         * update properly here and all of the reservation are deleted.
         */
        taskScheduler.schedule(() -> {
            Iterator<Reservation> reservations = showtime.getReservations().iterator();
            while (reservations.hasNext()) {
                Reservation reservation = reservations.next();
                Iterator<Seat> seats = reservation.getSeats().iterator();
                while (seats.hasNext()) {
                    Seat seat = seats.next();
                    seat.getReservations().clear();
                    seatRepository.save(seat);
                }
                reservationRepository.delete(reservation);
            }
            showtime.getReservations().clear();
            showtimeRepository.save(showtime);
        }, new Timestamp(showtime.getShowtimeDate().getTime() - fiveHours - 10000));

        /**
         * Here we change state of the showtime to <b>IN_PROGRESS</b> at
         * the start of its projection and we update everything that
         * corresponds to the showtime entity.
         */
        taskScheduler.schedule(() -> {
            showtime.setState(ShowtimeState.IN_PROGRESS);
            Iterator<Ticket> ticketIterator = showtime.getTickets().iterator();
            while(ticketIterator.hasNext()) {
                Ticket ticket = ticketIterator.next();
                ticket.setShow(showtime);
                ticketRepository.save(ticket);
            }
            Movie movie = showtime.getMovie();
            Iterator<Showtime> showtimeIterator = movie.getShows().iterator();
            while (showtimeIterator.hasNext()) {
                Showtime showtime1 = showtimeIterator.next();
                if(showtime1.getShowtimeId() == showtime.getShowtimeId()) {
                    showtime1.setState(showtime.getState());
                    break;
                }
            }
            MovieTheater movieTheater = showtime.getMovieTheater();
            Iterator<Showtime> showtimeIterator2 = movieTheater.getShows().iterator();
            while (showtimeIterator2.hasNext()) {
                Showtime showtime1 = showtimeIterator2.next();
                if(showtime1.getShowtimeId() == showtime.getShowtimeId()) {
                    showtime1.setState(showtime.getState());
                    break;
                }
            }
            movieTheaterRepository.save(movieTheater);
            movieRepository.save(movie);
        }, showtime.getShowtimeDate());

        /**
         * Here we change state of the showtime to <b>FINISHED</b> at
         * the end of its projection and we update everything that
         * corresponds to the showtime entity.
         */
        taskScheduler.schedule(() -> {
            showtime.setState(ShowtimeState.FINISHED);
            Iterator<Ticket> ticketIterator = showtime.getTickets().iterator();
            while(ticketIterator.hasNext()) {
                Ticket ticket = ticketIterator.next();
                ticket.setShow(showtime);
                ticketRepository.save(ticket);
            }
            Movie movie = showtime.getMovie();
            Iterator<Showtime> showtimeIterator = movie.getShows().iterator();
            while (showtimeIterator.hasNext()) {
                Showtime showtime1 = showtimeIterator.next();
                if(showtime1.getShowtimeId() == showtime.getShowtimeId()) {
                    showtime1.setState(showtime.getState());
                    break;
                }
            }
            MovieTheater movieTheater1 = showtime.getMovieTheater();
            Iterator<Showtime> showtimeIterator2 = movieTheater1.getShows().iterator();
            while (showtimeIterator2.hasNext()) {
                Showtime showtime1 = showtimeIterator2.next();
                if(showtime1.getShowtimeId() == showtime.getShowtimeId()) {
                    showtime1.setState(showtime.getState());
                    break;
                }
            }
            movieTheaterRepository.save(movieTheater1);
            movieRepository.save(movie);
        }, new Timestamp(showtime.getShowtimeDate().getTime() + (showtime.getMovie().getDuration() * 60 * 1000)));

        return new Response("Showtime for movie " + showtime.getMovie().getTitle() + " confirmed!");
    }

    /**
     * This method retrieves list of showtimes which are filtered by specified search pattern page by page.
     * Here movie id is provided to be matched with the showtime's movie id.
     * @see ShowtimeRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param movieId
     * @return <code>Collection</code> of showtimes.
     */
    @Override
    public Collection<ShowtimeDto> getShowsByMovie(Integer page, Integer size, Long movieId) {
        Iterable<Showtime> collection = showtimeRepository.getShowtimesByMovie(PageRequest.of(page, size), movieId);
        Iterator<Showtime> iterator = collection.iterator();
        List<ShowtimeDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(showtimeMapper.toDto(iterator.next()));
        }
        return newCollection;
    }

    /**
     * This method retrieves list of showtimes which are filtered by specified search pattern page by page.
     * Here cinema id is provided to be matched with the showtime's cinema id.
     * @see ShowtimeRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param cinemaId
     * @return <code>Collection</code> of showtimes.
     */
    @Override
    public Collection<ShowtimeDto> getShowsByCinema(Integer page, Integer size, Long cinemaId) {
        Iterable<Showtime> collection = showtimeRepository.getShowtimesByCinema(PageRequest.of(page, size), cinemaId);
        Iterator<Showtime> iterator = collection.iterator();
        List<ShowtimeDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(showtimeMapper.toDto(iterator.next()));
        }
        return newCollection;
    }

    /**
     * This method retrieves list of showtimes which are filtered by specified search pattern page by page.
     * Here projection date is provided to be matched with the showtime's projection date.
     * @see ShowtimeRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param showtimeDate
     * @return <code>Collection</code> of showtimes.
     */
    @Override
    public Collection<ShowtimeDto> getShowsByProjectionDate(Integer page, Integer size, Long showtimeDate) {
        Iterable<Showtime> collection = showtimeRepository.getShowtimesByShowtimeDate(PageRequest.of(page, size), new Date(showtimeDate));
        Iterator<Showtime> iterator = collection.iterator();
        List<ShowtimeDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(showtimeMapper.toDto(iterator.next()));
        }
        return newCollection;
    }

    /**
     * This method is responsible for creating reservation for movie showtime in database.
     * The description will be provided block by block below in the methods code.
     * @exception IllegalStateExceptionExtension
     * @exception RecordNotFoundException
     * @see ReservationMapper
     * @see CreateReservationDto
     * @param createReservationDto
     * @return <code>Collection</code> of showtimes.
     */
    @Override
    @Transactional(timeout = 20)
    public Response createReservation(CreateReservationDto createReservationDto) {
        /**
         * First we use pessimistic lock to lock showtime record for which we are giving the ticket.
         * We do this because we want the seat for this show to be reserved by only one person, and also
         * to resolve concurency when there is a race between reserving this seat and selling this seat at
         * the place.
         */
        Showtime showtime = showtimeRepository.getShowtimeByShowtimeIdLock(createReservationDto.getShowtimeId())
                .orElseThrow(() -> new RecordNotFoundException(Showtime.class.getSimpleName(), createReservationDto.getShowtimeId() + ""));
        long fiveHours = 5 * 60 * 60 * 1000;
        /**
         * Here we check if showtime state is CREATION so we cannot make reservation for that show...
         */
        if (showtime.getState().equals(ShowtimeState.CREATION))
            throw new IllegalStateExceptionExtension("You cannot create reservation for movie showtime that is not yet scheduled!");
        /**
         * Here we check if time before projection time is 5 hours or less sow we dont allow reservations in that period no more...
         */
        if (showtime.getShowtimeDate().getTime() - System.currentTimeMillis() < fiveHours)
            throw new IllegalStateExceptionExtension("You cannot make reservations 5 or less hours before the projection!");
        /**
         * Here we check if a user has already made a reservation for specified showtime because user can only
         * make one reservation per showtime
         */
        for (Reservation reservation : showtime.getReservations()) {
            if (((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername().equals(reservation.getUserEmail()))
                throw new IllegalStateExceptionExtension("You can make only one reservation per showtime!");
        }
        Reservation reservation = reservationMapper.toEntity(showtime, createReservationDto);
        /**
         * Next 2 for loops are here to provide check wheter the seat that we want to reserve is already reserved
         * or sold for this show...
         */
        for (int seatNumber : createReservationDto.getSeatNumbers()) {
            Seat seat = seatRepository.getSeatBySeatPK(new SeatPK(showtime.getMovieTheater().getMovieTheaterPK(), seatNumber))
                    .orElseThrow(() -> new RecordNotFoundException(Seat.class.getSimpleName(), showtime.getMovieTheater().getMovieTheaterPK().getCinemaId() + " "
                            + showtime.getMovieTheater().getMovieTheaterPK().getMtNumber() + " " + seatNumber));
            for (Reservation otherReservation : seat.getReservations()) {
                if (otherReservation.getShow().getShowtimeId() == showtime.getShowtimeId())
                    throw new IllegalStateExceptionExtension("Seat with number " + seat.getSeatPK().getSeatNumber() + " for this movie showtime is already reserved!");
            }
            for (Ticket ticket : showtime.getTickets()) {
                if (ticket.getSeat().getSeatPK().getSeatNumber() == seatNumber)
                    throw new IllegalStateExceptionExtension("Seat with number " + seat.getSeatPK().getSeatNumber() + " for this movie showtime is already sold!");
            }
            reservation.getSeats().add(seat);
            seat.getReservations().add(reservation);
        }
        if (reservation.getSeats().size() == 0) {
            throw new IllegalStateExceptionExtension("No seat numbers were specified for this reservation!");
        }
        /**
         * Finally we save both updated showtime and reservation that will
         * propagate saving to the seat entity.We alo send notification about
         * the reservation to the notification service, then to be send vie email
         * to the reservation's owner.
         */
        showtime.getReservations().add(reservation);
        showtimeRepository.save(showtime);
        reservationRepository.save(reservation);
        NotificationDto notification = NotificationDto
                .builder()
                .email(reservation.getUserEmail())
                .type(NotificationType.SHOWTIME_RESERVATION)
                .movie(showtime.getMovie().getTitle())
                .start(showtime.getShowtimeDate() + "")
                .build();
        amqpService.sendMessage(notification);
        return new Response("You're reservation with id:" + reservation.getReservationId() + " is created successfully!");
    }

    /**
     * This method is responsible for removing reservation from database.
     * We first check its existence.Only the user that made a reservation
     * can remove it, and that is provided with this search that requires
     * user's email and reservation id to match the exact reservation.
     * Then all entities that are corellated to the specified reservation
     * are being updated and finally we delete the reservation.Also after
     * that a notification is will be sent to notification service and then
     * that notification will be sent to reservation's owner.
     * @exception RecordNotFoundException
     * @param reservationId
     * @return <code>Collection</code> of showtimes.
     */
    @Override
    public Response deleteReservation(Long reservationId) {
        Reservation reservation = reservationRepository
                .getReservationByUserEmailAndReservationId(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(), reservationId)
                .orElseThrow(() -> new RecordNotFoundException(Reservation.class.getSimpleName(), reservationId + ""));
        Showtime showtime = reservation.getShow();
        showtime.getReservations().remove(reservation);
        for (Seat seat : reservation.getSeats()) {
            seat.getReservations().remove(reservation);
            seatRepository.save(seat);
        }
        showtimeRepository.save(showtime);
        reservationRepository.delete(reservation);
        NotificationDto notification = NotificationDto
                .builder()
                .email(reservation.getUserEmail())
                .type(NotificationType.RESERVATION_CANCELED)
                .build();
        amqpService.sendMessage(notification);
        return new Response("You're reservation with id:" + reservation.getReservationId() + " is deleted successfully!");
    }

    /**
     * The same method as the one above but without
     * the part of sending notification...This method
     * is usually called by an admin from the user
     * service when deleting user.
     * @param email
     * @return <code>Collection</code> of showtimes.
     */
    @Override
    public Response deleteReservationAdmin(String email) {
        List<Reservation> reservations = reservationRepository
                .getReservationsByUserEmail(email);
        for (Reservation reservation : reservations) {
            Showtime showtime = reservation.getShow();
            showtime.getReservations().remove(reservation);
            for (Seat seat : reservation.getSeats()) {
                seat.getReservations().remove(reservation);
                seatRepository.save(seat);
            }
            showtimeRepository.save(showtime);
            reservationRepository.delete(reservation);
        }
        return new Response("Deletion of user with email:" + email + " successful!");
    }

    /**
     * This method is responsible for creating reservation for movie showtime in database.
     * The description will be provided block by block below in the methods code.
     * @exception IllegalStateExceptionExtension
     * @exception RecordNotFoundException
     * @see TicketMapper
     * @see CreateTicketDto
     * @param ticketDto
     * @return <code>Collection</code> of tickets.
     */
    @Transactional(timeout = 20)
    @Override
    public Response createTicket(CreateTicketDto ticketDto) {
        /**
         * First we use pessimistic lock to lock showtime record for which we are giving the ticket.
         * We do this because we want the seat for this show to be sold to only one person, and also
         * to resolve concurency when there is a race between reserving a seat and selling a seat at
         * the place.
         */
        Showtime showtime = showtimeRepository.getShowtimeByShowtimeIdLock(ticketDto.getShowtimeId())
                .orElseThrow(() -> new RecordNotFoundException(Showtime.class.getSimpleName(), ticketDto.getShowtimeId() + ""));
        Seat seat = seatRepository.getSeatBySeatPK(new SeatPK(showtime.getMovieTheater().getMovieTheaterPK(), ticketDto.getSeatNumber()))
                .orElseThrow(() -> new RecordNotFoundException(Seat.class.getSimpleName(), ticketDto.getSeatNumber() + ""));
        /**
         * Here we check if showtime has ended because then we cant sold tickets for the show that has ended.
         */
        if (showtime.getState().equals(ShowtimeState.FINISHED))
            throw new IllegalStateExceptionExtension("Cannot create ticket for this movie showtime because it is finished!");
        /**
         * Here we check if showtime is not yet scheduled.
         */
        if (showtime.getState().equals(ShowtimeState.CREATION))
            throw new IllegalStateExceptionExtension("Cannot create ticket for this movie showtime because it is not yet scheduled!");
        /**
         * Here we perform check if a selected seat is maybe already sold or reserved.
         */
        for (Reservation otherReservation : seat.getReservations()) {
            if (otherReservation.getShow().getShowtimeId() == showtime.getShowtimeId())
                throw new IllegalStateExceptionExtension("Seat with number " + seat.getSeatPK().getSeatNumber() + " for this movie showtime is already reserved!");
        }
        for (Ticket ticket : showtime.getTickets()) {
            if (ticket.getSeat().getSeatPK().getSeatNumber() == seat.getSeatPK().getSeatNumber())
                throw new IllegalStateExceptionExtension("Seat number " + ticket.getSeat().getSeatPK().getSeatNumber() + " for this movie showtime is already sold!");
        }
        Ticket ticket = ticketMapper.toEntity(showtime, seat);
        showtime.getTickets().add(ticket);
        showtimeRepository.save(showtime);
        Long ticketId = ticketRepository.save(ticket).getTicketId();
        return new Response("Ticket with id:" + ticketId + " created successfully!");
    }

    /**
     * Method responsible for deleting ticket records from database.
     * First we search for its existence in database based on
     * provided ticket id, then we update showtime's list of
     * tickets and finally we delete specified ticket.
     * @exception RecordNotFoundException
     * @param ticketId
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response deleteTicket(Long ticketId) {
        Ticket ticket = ticketRepository.getTicketByTicketId(ticketId)
                .orElseThrow(() -> new RecordNotFoundException(Ticket.class.getSimpleName(), ticketId + ""));
        Showtime showtime = ticket.getShow();
        showtime.getTickets().remove(ticket);
        ticketRepository.delete(ticket);
        showtimeRepository.save(showtime);
        return new Response("Ticket with id:" + ticketId + " deleted successfully!");
    }


}
