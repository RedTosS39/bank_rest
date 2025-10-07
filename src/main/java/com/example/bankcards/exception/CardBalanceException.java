package com.example.bankcards.exception;

public class CardBalanceException extends RuntimeException {
    public CardBalanceException(String message) {
        super(message);
    }
}
