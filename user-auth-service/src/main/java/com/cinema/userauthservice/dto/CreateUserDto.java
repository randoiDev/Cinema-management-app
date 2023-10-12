package com.cinema.userauthservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserDto {

    //Same as in CreateEmployeeDto class
    @Pattern(regexp = "^[a-zA-Z\\d._%+-]{1,64}@gmail\\.com$",message = "Email address is not valid!")
    private String email;
    //Password requirements are given in the message attribute of the below @Pattern annotation
    @Pattern(regexp = "^(?=.*\\d)[a-zA-Z\\d]{8,16}$",message = "Password must contain only a-z or A-Z characters, at least 1 number " +
            "and to be from 8 - 16 characters long!")
    private String password;
    @NotBlank(message = "Name is not valid!")
    private String name;
    @NotBlank(message = "Surname is not valid!")
    private String surname;

}
