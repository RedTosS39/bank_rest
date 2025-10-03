package com.example.bankcards.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bank_card")
public class CardEntity {
    @Id
    @Column(name = "bank_card_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bankCardId;

    @Column(name = "expired_at")
    private LocalDate expiredDate;

    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "card_holder_id", referencedColumnName = "user_id")
    private UserEntity userEntity;

}
