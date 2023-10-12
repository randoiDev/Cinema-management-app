package com.cinema.userauthservice.dto;

import com.cinema.userauthservice.domain.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeDto {

    private Long id;
    private String email;
    private String name;
    private String surname;
    private Long birthDate;
    private String phone;
    private String idCardNumber;
    private Long cinemaId;
    private UserRole role;
    private Long managerId;

}
