package com.cinema.userauthservice.security.exceptions.handler;

import com.cinema.userauthservice.dto.SecurityErrorResponse;
import com.cinema.userauthservice.utils.JsonBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * @<code>CustomAuthenticationEntryPoint</code> - Is a customized class for intercepting <code>AuthenticationException</code>
 * and reformatting response that will be sent back to the client based on different type of exception that was thrown.
 */

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonBuilder builder = new JsonBuilder();
        Map<String, Object> json;
        if (authenticationException instanceof LockedException) {
            json = builder
                    .put("timestamp", Date.from(Instant.now()).toString())
                    .put("error", "You must first verify your account!")
                    .build();
        } else if (authenticationException instanceof BadCredentialsException) {
            json = builder
                    .put("timestamp", Date.from(Instant.now()).toString())
                    .put("error", "Incorrect email or password!")
                    .build();
        } else {
            json = builder
                    .put("timestamp", Date.from(Instant.now()).toString())
                    .put("error", authenticationException.getMessage())
                    .build();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), new SecurityErrorResponse(json));

    }
}
