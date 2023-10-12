package com.cinema.userauthservice.dto;

import com.cinema.userauthservice.domain.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Long id;
    private String email;
    private String name;
    private String surname;

}
