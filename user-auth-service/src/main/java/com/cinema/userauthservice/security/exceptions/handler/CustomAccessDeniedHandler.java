package com.cinema.userauthservice.security.exceptions.handler;

import com.cinema.userauthservice.dto.SecurityErrorResponse;
import com.cinema.userauthservice.utils.JsonBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * @<code>CustomAccessDeniedHandler</code> - Is a customized class for intercepting <code>AccessDeniedException</code>
 * and reformatting response that will be sent back to the client.
 */

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        JsonBuilder builder = new JsonBuilder();
        Map<String, Object> json = builder
                .put("timestamp", Date.from(Instant.now()).toString())
                .put("error", accessDeniedException.getMessage())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), new SecurityErrorResponse(json));
    }
}
