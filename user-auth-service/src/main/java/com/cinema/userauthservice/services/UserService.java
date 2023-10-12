package com.cinema.userauthservice.services;

import com.cinema.userauthservice.domain.ConfirmationToken;
import com.cinema.userauthservice.domain.User;
import com.cinema.userauthservice.dto.*;

import java.util.Collection;

public interface UserService {

    LoginResponse login(LoginRequest loginRequest);

    Response createUser(CreateUserDto createUserDto);

    Response deleteUser(Long id, String authorization);

    Collection<UserDto> filterUsers(Integer page, Integer size, String searchPattern);

    ConfirmationToken createConfirmationToken(User user);

    Response confirmToken(String token);

}
