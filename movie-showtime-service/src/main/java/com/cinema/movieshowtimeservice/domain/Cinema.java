package com.cinema.movieshowtimeservice.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @<code>Cinema</code> - Entity that represents an cinema.
 * Attributes:
 * <ul>
 *     <li><code>name</code> - Cinemas name</li>
 *     <li><code>email</code> - Cinemas email</li>
 *     <li><code>phone</code> - Cinemas phone number based on Serbia phone number format</li>
 *     <li><code>address</code> - Cinemas address</li>
 *     <li><code>city</code> - City in which cinema is</li>
 *     <li><code>movieTheaters</code> - Movie theaters that belong to the specific cinema</li>
 * </ul>
 */

@SuppressWarnings("all")
@Entity
@Table(name = "cinema")
@Data
@NoArgsConstructor
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cinema_id", nullable = false)
    private Long cinemaId;
    @Column(length = 30, nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(length = 15, nullable = false, unique = true)
    private String phone;
    @Column(length = 100, nullable = false)
    private String address;
    @Column(length = 50, nullable = false)
    private String city;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cinema")
    private Collection<MovieTheater> movieTheaters = new ArrayList<>();

}
