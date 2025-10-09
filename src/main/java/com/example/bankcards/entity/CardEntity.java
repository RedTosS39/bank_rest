package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "bank_card")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CardEntity {
    @Id
    @Column(name = "bank_card_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_number", nullable = false, columnDefinition = "CHAR(16)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private String cardNumber;

    @Column(name = "expired_at")
    private LocalDate expiredDate;

    @Column(name = "balance", precision = 12, scale = 5)
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity userEntity;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "version")
    @Version
    private int version;
}
