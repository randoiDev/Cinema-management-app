package com.cinema.movieshowtimeservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SecurityErrorResponse {
    private String timestamp;
    private String error;

    public SecurityErrorResponse(Map<String, Object> json) {
        this.timestamp = json.get("timestamp").toString();
        this.error = json.get("error").toString();
    }

}
