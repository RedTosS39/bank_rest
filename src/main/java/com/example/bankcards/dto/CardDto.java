package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonIgnore
    private String cardNumber;
    private LocalDate expiredDate;
    private BigDecimal balance;
    private Status status;

    @JsonProperty("cardNumber")
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 16) {
            return cardNumber;
        }
        String cleanNumber = cardNumber.replaceAll("\\s+", "");
        if (cleanNumber.length() < 16) {
            return cardNumber;
        }

        String lastFour = cleanNumber.substring(cleanNumber.length() - 4);
        return "**** **** **** " + lastFour;
    }
}
