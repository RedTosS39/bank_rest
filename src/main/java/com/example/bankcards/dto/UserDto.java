package com.example.bankcards.dto;

import com.example.bankcards.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Schema(description = "User ID (auto-generated, not required for registration)", example = "null", required = false)
    private Long id;
    private String username;
    private String password;
    private Role role;

    @Schema(description = "User's cards not required for registration)", example = "null", required = false)
    @JsonIgnore
    private List<CardDto> cards;
}
