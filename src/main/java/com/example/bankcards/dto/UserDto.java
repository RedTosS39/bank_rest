package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Role role;
    private List<CardDto> cards;
}
