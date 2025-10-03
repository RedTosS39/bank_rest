package com.example.bankcards.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int cardHolderId;

    @Column(name = "card_owner")
    private String cardOwner;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "userEntity")
    private List<CardEntity> cards;
}
