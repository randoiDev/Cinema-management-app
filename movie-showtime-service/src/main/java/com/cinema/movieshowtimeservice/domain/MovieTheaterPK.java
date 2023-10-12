package com.cinema.movieshowtimeservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@SuppressWarnings("all")
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieTheaterPK implements Serializable {

    @Column(name = "cinema_id", nullable = false)
    private Long cinemaId;
    @Column(name = "mt_number", nullable = false)
    private Integer mtNumber;

}
