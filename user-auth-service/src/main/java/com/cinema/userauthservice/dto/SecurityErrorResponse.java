package com.cinema.userauthservice.dto;

import lombok.Data;

import java.util.Map;

/**
 * <code>SecurityErrorResponse</code> - wrapper class that represents error responses coming from security filter checks
 */

@Data
public class SecurityErrorResponse {
    private String timestamp;
    private String error;

    public SecurityErrorResponse(Map<String, Object> json) {
        this.timestamp = json.get("timestamp").toString();
        this.error = json.get("error").toString();
    }

}
