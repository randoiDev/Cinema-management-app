package com.cinema.userauthservice.services.imp;

import com.cinema.userauthservice.domain.ConfirmationToken;
import com.cinema.userauthservice.domain.User;
import com.cinema.userauthservice.domain.enums.UserRole;
import com.cinema.userauthservice.dto.*;
import com.cinema.userauthservice.dto.enums.NotificationType;
import com.cinema.userauthservice.exceptions.IllegalStateExceptionExtension;
import com.cinema.userauthservice.exceptions.RecordNotFoundException;
import com.cinema.userauthservice.mappers.UserMapper;
import com.cinema.userauthservice.repositories.ConfirmationTokenRepository;
import com.cinema.userauthservice.repositories.UserRepository;
import com.cinema.userauthservice.security.jwt.JwtService;
import com.cinema.userauthservice.services.AMQPService;
import com.cinema.userauthservice.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @<code>UserServiceImp</code> - Class that has methods that are responsible for invoking business logic for a specific endpoint call.
 */

@SuppressWarnings("all")
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserAuthenticationService userAuthenticationService;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final AMQPService amqpService;
    private final RestTemplate restTemplate;
    @Value("${cinema-service.host}")
    private String cinemaHostname;

    /**
     * This method is responsible for removing non enabled users from user table every day at midnight,
     * so that the table stays clean from "garbage".
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void confirmationTokenScheduledRemoval() {
        userRepository.removeUsersByEnabledIsFalse();
    }

    /**
     * This method is responsible for user authentication,and if everything is okay,
     * it's invoking the creation of user's unique JWT token.
     * @see LoginRequest
     * @param loginRequest
     * @see LoginResponse
     * @return JWT wrapped in <code>LoginResponse</code> class.
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        UserDetails user = userAuthenticationService.loadUserByUsername(loginRequest.getEmail());
        return new LoginResponse(jwtService.generateToken(Map.of("roles", user.getAuthorities()), user));
    }

    /**
     * This method is responsible for creating ordinary users of this app.
     * First user mapper maps fields from <code>CreateUserDto</code> to a user entity,
     * then it invokes method for creating <code>ConfirmationToken</code> token that needs
     * to be confirmed in order for user to use his account.Then it sends notification to a
     * notification service which will send the account verification email to a specified email
     * within registration request.
     * @see UserMapper
     * @see CreateUserDto
     * @param createUserDto
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response createUser(CreateUserDto createUserDto) {
        createUserDto.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        User u = userMapper.toEntity(createUserDto);
        ConfirmationToken token = createConfirmationToken(u);
        u.getTokens().add(token);
        userRepository.save(u);
        String link = "http://localhost:8080/api/authentication/confirm?token=" + token.getToken();
        NotificationDto notification = NotificationDto
                .builder()
                .email(createUserDto.getEmail())
                .name(createUserDto.getName())
                .link(link)
                .type(NotificationType.ACCOUNT_VERIFICATION)
                .build();
        amqpService.sendMessage(notification);
        return new Response("Verification mail was sent to your gmail account!");
    }

    /**
     * This method is responsible for user ordinary user deletion,
     * it first checks if the user is maybe something else than <b>USER</b> so it will forbid that action,
     * then it deletes user and calls the service that is responsible for storing info
     * on showtime reservations that are made by these ordinary users and delete them if
     * they exist.The other way of defining this method is to put the deletion of a user at
     * the end of this method, after the rest api call to this other service so no roll back
     * would be needed if an error occurs.This method is also provided with authority parameter
     * because the called api endpoint needs <b>ADMIN</b> privilege to be invoked.
     * Also it was maybe better not to hardcore this link string?
     * @exception RecordNotFoundException
     * @exception IllegalStateExceptionExtension
     * @param userId
     * @param authorization
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    @Transactional(rollbackFor = {HttpClientErrorException.class, HttpServerErrorException.class, ResourceAccessException.class})
    public Response deleteUser(Long userId, String authorization) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException(User.class.getSimpleName(), userId + ""));
        if(!user.getRole().equals(UserRole.USER))
            throw new IllegalStateExceptionExtension("You can only delete ordinary user!");
        userRepository.delete(user);
        String jwt = authorization.split(" ")[1];
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        HttpEntity httpEntity = new HttpEntity(null, httpHeaders);
        String uri = "http://" + cinemaHostname + ":8081/api/showtime/reservation_admin?email=" + user.getEmail();
        ResponseEntity<Response> response = restTemplate.exchange(uri, HttpMethod.DELETE, httpEntity, Response.class);
        return response.getBody();

    }

    /**
     * This method retrieves list of enabled users which are filtered by specified search pattern page by page.
     * Search pattern is matched with users name, surname and email.
     * @see UserRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param searchPattern
     * @return <code>Collection</code> of users.
     */
    @Override
    public Collection<UserDto> filterUsers(Integer page, Integer size, String searchPattern) {
        Iterable<User> collection = userRepository.filterUsers(PageRequest.of(page, size), searchPattern);
        Iterator<User> iterator = collection.iterator();
        List<UserDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            User user = iterator.next();
            if(user.isEnabled()) {
                newCollection.add(userMapper.toDto(user));
            }
        }
        return newCollection;
    }

    /**
     * This method is responsible for creating confirmation token.
     * I didn't use mapper for this one because the mapping for token is only
     * performed here.
     * @see User
     * @param user
     * @return <code>ConfirmationToken</code> instance
     */
    @Override
    public ConfirmationToken createConfirmationToken(User user) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(UUID.randomUUID().toString());
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setUser(user);
        return confirmationToken;
    }

    /**
     * This method is responsible for checking if provided token is valid,
     * then checking if this token is already confirmed or it has expire and if
     * everything is okay, it enables the linked user account.
     * If something is not okay it throws specified exceptions.
     * @exception  RecordNotFoundException
     * @exception IllegalStateExceptionExtension
     * @param token
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new RecordNotFoundException(ConfirmationToken.class.getSimpleName(), token));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateExceptionExtension("This email is already confirmed!");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        LocalDateTime confirmedAt = LocalDateTime.now();
        if (expiredAt.isBefore(confirmedAt)) {
            throw new IllegalStateExceptionExtension("This token has expired!");
        }
        confirmationToken.setConfirmedAt(confirmedAt);
        User user = userRepository.findByEmail(confirmationToken.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        confirmationTokenRepository.save(confirmationToken);
        return new Response("Your account has been verified!");
    }

}
