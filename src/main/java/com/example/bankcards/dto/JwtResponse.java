package com.example.bankcards.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String username;
    private String role;

    public JwtResponse() {
    }

    public JwtResponse(String token, String type, String username, String role) {
        this.token = token;
        this.type = type;
        this.username = username;
        this.role = role;
    }

}
