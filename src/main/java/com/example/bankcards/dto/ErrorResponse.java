package com.example.bankcards.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponse {

    private int status;
    private String timestamp;
    private String message;

    public ErrorResponse(int status, String timestamp, String message) {
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
    }
}
