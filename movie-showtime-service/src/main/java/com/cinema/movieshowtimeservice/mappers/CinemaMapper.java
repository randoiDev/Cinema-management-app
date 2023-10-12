package com.cinema.movieshowtimeservice.mappers;

import com.cinema.movieshowtimeservice.domain.Cinema;
import com.cinema.movieshowtimeservice.dto.cinema.AddCinemaDto;
import com.cinema.movieshowtimeservice.dto.cinema.CinemaDto;
import com.cinema.movieshowtimeservice.dto.cinema.UpdateCinemaDto;
import org.springframework.stereotype.Component;

@Component
public class CinemaMapper {

    public Cinema toEntityFromCreation(AddCinemaDto cinemaDto) {
        Cinema cinema = new Cinema();
        cinema.setName(cinemaDto.getName());
        cinema.setAddress(cinemaDto.getAddress());
        cinema.setCity(cinemaDto.getCity());
        cinema.setEmail(cinemaDto.getEmail());
        cinema.setPhone(cinemaDto.getPhone());
        return cinema;
    }

    public Cinema toEntityFromUpdate(Cinema cinema, UpdateCinemaDto cinemaDto) {
        if (cinemaDto.getName() != null && !cinemaDto.getName().isBlank()) {
            cinema.setName(cinemaDto.getName());
        }
        if (cinemaDto.getAddress() != null && !cinemaDto.getAddress().isBlank()) {
            cinema.setAddress(cinemaDto.getAddress());
        }
        if (cinemaDto.getEmail() != null) {
            cinema.setEmail(cinemaDto.getEmail());
        }
        if (cinemaDto.getPhone() != null) {
            cinema.setPhone(cinemaDto.getPhone());
        }
        if (cinemaDto.getCity() != null && !cinemaDto.getCity().isBlank()) {
            cinema.setCity(cinemaDto.getCity());
        }
        return cinema;
    }

    public CinemaDto toDto(Cinema cinema) {
        return CinemaDto
                .builder()
                .name(cinema.getName())
                .address(cinema.getAddress())
                .city(cinema.getCity())
                .phone(cinema.getPhone())
                .email(cinema.getEmail())
                .cinemaId(cinema.getCinemaId())
                .build();
    }

}
