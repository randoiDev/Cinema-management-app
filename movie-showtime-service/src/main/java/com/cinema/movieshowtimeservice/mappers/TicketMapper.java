package com.cinema.movieshowtimeservice.mappers;

import com.cinema.movieshowtimeservice.domain.Seat;
import com.cinema.movieshowtimeservice.domain.Showtime;
import com.cinema.movieshowtimeservice.domain.Ticket;
import com.cinema.movieshowtimeservice.domain.enums.SeatType;
import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class TicketMapper {

    public Ticket toEntity(Showtime showtime, Seat seat) {
        Ticket ticket = new Ticket();
        if(seat.getSeat().equals(SeatType.ECONOMY)) {
            ticket.setPrice(showtime.getEconomyPrice());
        } else if(seat.getSeat().equals(SeatType.VIP)) {
            ticket.setPrice(showtime.getVipPrice());
        }
        ticket.setPurchaseDate(new Date(System.currentTimeMillis()));
        ticket.setSeat(seat);
        ticket.setShow(showtime);
        return ticket;
    }

}
