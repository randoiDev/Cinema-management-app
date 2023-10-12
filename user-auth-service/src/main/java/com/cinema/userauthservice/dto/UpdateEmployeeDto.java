package com.cinema.userauthservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateEmployeeDto {

    @NotNull(message = "Manager id is not valid!")
    private Long managerId;
    @Pattern(regexp = "^[a-zA-Z\\d._%+-]{1,64}@gmail\\.com$", message = "Email address is not valid!")
    private String email;
    @NotBlank(message = "Name is not valid!")
    private String name;
    @NotBlank(message = "Surname is not valid!")
    private String surname;
    @NotNull(message = "Birth date must be specified!")
    private Long birthDate;
    @Pattern(regexp = "^\\+3816\\d{7,8}$", message = "Phone number is not valid!")
    private String phone;
    @Pattern(regexp = "^\\d{9}$", message = "Id card number is not valid!")
    private String idCardNumber;
    @NotNull(message = "Cinema id is not valid!")
    private Long cinemaId;

}
