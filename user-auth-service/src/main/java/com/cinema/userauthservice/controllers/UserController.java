package com.cinema.userauthservice.controllers;

import com.cinema.userauthservice.dto.Response;
import com.cinema.userauthservice.dto.UserDto;
import com.cinema.userauthservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @<code>UserController</code> - Spring rest controller for accessing endpoints for manipulating user data.
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * @see com.cinema.userauthservice.domain.enums.UserRole Endpoint that requires <b>ADMIN</b> privilege to be accessed.
     * It is responsible for calling user service to delete a user with specified id.
     * @param userId
     * @param authorization
     * @see Response
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> deleteUser(@RequestParam("userId") Long userId, @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(userService.deleteUser(userId, authorization));
    }

    /**
     * Endpoint that requires <b>ADMIN</b> privilege to be accessed.
     * It is responsible for calling user service to retrieve a list of users, the filtration is done
     * by matching provided search pattern with names, surnames and emails of users.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param searchPattern
     * @see UserDto
     * @return <code>Collection</code> of users that are registered in the system.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionUserResponse")
            }
    )
    public ResponseEntity<Iterable<UserDto>> filterUsers(@RequestParam(defaultValue = "0") Integer page,
                                                         @RequestParam(defaultValue = "2") Integer size,
                                                         @RequestParam("searchPattern") String searchPattern) {
        return ResponseEntity.ok(userService.filterUsers(page, size, searchPattern));
    }
}
