package com.cinema.movieshowtimeservice.dto.cinema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class UpdateCinemaDto {

    @NotNull(message = "Cinema id is not valid!")
    private Long cinemaId;
    private String name;
    //Emails must have local parts which are no longer than 64 and domain that is "@gmail.com".
    @Nullable
    @Pattern(regexp = "^(([a-zA-Z\\d._%+-]{1,64}@gmail\\.com))$",message = "Cinemas email address is not valid!")
    private String email;
    //Phone numbers are in Serbia phone number format which are "+3816" then followed by 7 or 8 digits.
    @Nullable
    @Pattern(regexp = "^(\\+3816\\d{7,8})$",message = "Cinemas phone number is not valid!")
    private String phone;
    private String address;
    private String city;

}
