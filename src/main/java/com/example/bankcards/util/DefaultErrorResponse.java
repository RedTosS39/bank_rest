package com.example.bankcards.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DefaultErrorResponse {
    private long timestamp;
    private String message;

    public DefaultErrorResponse(long timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }
}
