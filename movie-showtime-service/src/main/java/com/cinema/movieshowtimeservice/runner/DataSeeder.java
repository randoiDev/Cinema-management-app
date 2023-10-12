package com.cinema.movieshowtimeservice.runner;

import com.cinema.movieshowtimeservice.domain.*;
import com.cinema.movieshowtimeservice.repositories.CinemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CinemaRepository cinemaRepository;

    @Override
    public void run(String... args) throws Exception {

        Cinema cinema = new Cinema();
        cinema.setName("Cinema 1");
        cinema.setAddress("Address 1");
        cinema.setCity("City 1");
        cinema.setPhone("+38166111122");
        cinema.setEmail("cinema1@gmail.com");

        cinemaRepository.save(cinema);

    }
}
