package com.cinema.userauthservice.mappers;

import com.cinema.userauthservice.domain.User;
import com.cinema.userauthservice.domain.enums.UserRole;
import com.cinema.userauthservice.dto.CreateUserDto;
import com.cinema.userauthservice.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserDto createUserDto) {
        User u = new User();
        u.setEmail(createUserDto.getEmail());
        u.setPassword(createUserDto.getPassword());
        u.setName(createUserDto.getName());
        u.setSurname(createUserDto.getSurname());
        u.setRole(UserRole.USER);
        return u;
    }

    public UserDto toDto(User user) {
        return UserDto
                .builder()
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .id(user.getId())
                .build();
    }
}
