package com.example.bankcards.service;

import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.Status;
import com.example.bankcards.exception.CardAlreadyExistException;
import com.example.bankcards.exception.CardBalanceException;
import com.example.bankcards.exception.CardExpiredException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransferMoneyService {

    private final CardRepository cardRepository;

    @Autowired
    public TransferMoneyService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public void transferMoney(Long from, Long to, BigDecimal amount) {
        CardEntity cardFrom = getCard(from);
        CardEntity cardTo = getCard(to);

        if (!cardFrom.getStatus().equals(Status.ACTIVE)
            || !cardTo.getStatus().equals(Status.ACTIVE)) {
            throw new CardExpiredException("Card is expired or blocked");
        }

        if (cardFrom.getExpiredDate().isBefore(LocalDate.now())) {
            throw new CardExpiredException("Card is expired");
        }

        if (!(cardFrom.getBalance().compareTo(amount) >= 0)) {
            throw new CardBalanceException("Not enough balance");
        }


        cardFrom.setBalance(cardFrom.getBalance().subtract(amount));
        cardTo.setBalance(cardTo.getBalance().add(amount));
    }

    private CardEntity getCard(Long from) {
        return cardRepository.findByIdWithLock(from).orElseThrow(() -> new CardNotFoundException("Card not found"));
    }
}
