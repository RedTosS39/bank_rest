package com.example.bankcards.service;

import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.Status;
import com.example.bankcards.exception.CardBalanceException;
import com.example.bankcards.exception.CardExpiredException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferMoneyService {

    private final CardRepository cardRepository;

    @Autowired
    public TransferMoneyService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public void transferMoney(Long from, Long to, BigDecimal amount) {
        CardEntity cardFrom = cardRepository.findById(from).orElseThrow(() -> new CardNotFoundException("Card not found"));
        CardEntity cardTo = cardRepository.findById(to).orElseThrow(() -> new CardNotFoundException("Card not found"));
        if (!(cardFrom.getBalance().compareTo(amount) >= 0)) {
            throw new CardBalanceException("Not enough balance");
        }

        if (cardFrom.getStatus().equals(Status.BLOCKED)
            || cardFrom.getStatus().equals(Status.EXPIRED)
            || cardTo.getStatus().equals(Status.BLOCKED)
            || cardTo.getStatus().equals(Status.EXPIRED)) {
            throw new CardExpiredException("Card is expired or blocked");
        }

        cardFrom.setBalance(cardFrom.getBalance().subtract(amount));
        cardTo.setBalance(cardTo.getBalance().add(amount));
    }
}
