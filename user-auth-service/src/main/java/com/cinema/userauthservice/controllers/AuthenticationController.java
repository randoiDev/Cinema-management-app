package com.cinema.userauthservice.controllers;

import com.cinema.userauthservice.dto.LoginRequest;
import com.cinema.userauthservice.dto.LoginResponse;
import com.cinema.userauthservice.dto.Response;
import com.cinema.userauthservice.dto.CreateUserDto;
import com.cinema.userauthservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @<code>AuthenticationController</code> - Spring rest controller for accessing endpoints for user registration,
 * account confirmation and authentication.
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    /**
     * Endpoint that invokes user authentication logic method for actual authentication from <code>UserService</code>
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulLoginResponse")
            }
    )
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    /**
     * Endpoint for user registration on this platform.
     * @see CreateUserDto
     * @param createUserDto
     * @see Response
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PostMapping("/register")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        return ResponseEntity.ok(userService.createUser(createUserDto));
    }

    /**
     * Endpoint for user account verification confirmation.
     * @param token
     * @see Response
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @GetMapping("/confirm")
    @Operation(
            hidden = true
    )
    public ResponseEntity<Response> confirmRegistration(@RequestParam("token") String token) {
        return ResponseEntity.ok(userService.confirmToken(token));
    }

}
