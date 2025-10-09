package com.example.bankcards.dto;

import com.example.bankcards.entity.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {

    @Schema(description = "Card id not required for registration)", example = "null", required = false)
    private Long id;

    @Schema(description = "user id not required for registration)", example = "null", required = false)
    @JsonIgnore
    private UserDto user;

    @JsonIgnore
    private String cardNumber;

    @Schema(description = "expiredDate will assign when assigning to user", example = "null", required = false)
    private LocalDate expiredDate;

    @Schema(description = "balance  not required for creating card", example = "null", required = false)
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
