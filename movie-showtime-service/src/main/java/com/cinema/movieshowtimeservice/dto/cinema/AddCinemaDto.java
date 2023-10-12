package com.cinema.movieshowtimeservice.dto.cinema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddCinemaDto {

    @NotBlank(message = "Cinemas name is not valid!")
    private String name;
    //Emails must have local parts which are no longer than 64 and domain that is "@gmail.com".
    @Pattern(regexp = "^[a-zA-Z\\d._%+-]{1,64}@gmail\\.com$",message = "Cinemas email address is not valid!")
    private String email;
    //Phone numbers are in Serbia phone number format which are "+3816" then followed by 7 or 8 digits.
    @Pattern(regexp = "^\\+3816\\d{7,8}$",message = "Cinemas phone number is not valid!")
    private String phone;
    @NotBlank(message = "Cinemas address is not valid!")
    private String address;
    @NotBlank(message = "City name in which cinema is is not valid!")
    private String city;

}
