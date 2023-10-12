package com.cinema.userauthservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateEmployeeDto {

    //Emails must have local parts which are no longer than 64 and domain that is "@gmail.com".
    @Pattern(regexp = "^[a-zA-Z\\d._%+-]{1,64}@gmail\\.com$",message = "Email address is not valid!")
    private String email;
    @NotBlank(message = "Name is not valid!")
    private String name;
    @NotBlank(message = "Surname is not valid!")
    private String surname;
    @NotNull(message = "Birth date must be specified!")
    private Long birthDate;
    //Phone numbers are in Serbia phone number format which are "+3816" then followed by 7 or 8 digits.
    @Pattern(regexp = "^\\+3816\\d{7,8}$",message = "Phone number is not valid!")
    private String phone;
    //ID card numbers are in Serbia id card number format which must be 9 digits long
    @Pattern(regexp = "^\\d{9}$",message = "Id card number is not valid!")
    private String idCardNumber;
    @NotNull(message = "Cinema id is not valid!")
    private Long cinemaId;
    private Long managerId;

}
