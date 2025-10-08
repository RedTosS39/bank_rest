package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
public class CardDto {
    private Long id;
    private UserDto user;
    private String cardNumber;
    private LocalDate expiredDate;
    private BigDecimal balance;
    private Status status;
}
