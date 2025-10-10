package com.example.bankcards.service;

import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.Status;
import com.example.bankcards.exception.CardBalanceException;
import com.example.bankcards.exception.CardExpiredException;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = TransferMoneyService.class)
class TransferMoneyServiceTest {

    private static final Long CARD_FROM_ID = 1L;
    private static final Long CARD_TO_ID = 2L;
    private static final BigDecimal BALANCE_FROM = BigDecimal.valueOf(500);
    private static final BigDecimal BALANCE_FROM_NOT_ENOUGH = BigDecimal.valueOf(1);
    private static final BigDecimal BALANCE_TO = BigDecimal.valueOf(100);
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(500);
    private static final BigDecimal EXPECTED_BALANCE_FROM = BigDecimal.valueOf(0);
    private static final BigDecimal EXPECTED_BALANCE_TO = BigDecimal.valueOf(600);


    @MockitoBean
    private CardRepository cardRepository;
    @Autowired
    private TransferMoneyService transferMoneyService;

    @Test
    void TransferMoneyService_Transfer_Money_Success() {
        CardEntity cardFrom = mock(CardEntity.class);
        CardEntity cardTo = mock(CardEntity.class);

        when(cardFrom.getBalance()).thenReturn(BALANCE_FROM);
        when(cardFrom.getStatus()).thenReturn(Status.ACTIVE);
        when(cardTo.getBalance()).thenReturn(BALANCE_TO);
        when(cardTo.getStatus()).thenReturn(Status.ACTIVE);

        LocalDate futureDate = LocalDate.now().plusYears(1);

        when(cardFrom.getExpiredDate()).thenReturn(futureDate);
        when(cardTo.getExpiredDate()).thenReturn(futureDate);
        when(cardRepository.findByIdWithLock(CARD_FROM_ID)).thenReturn(Optional.of(cardFrom));
        when(cardRepository.findByIdWithLock(CARD_TO_ID)).thenReturn(Optional.of(cardTo));

        transferMoneyService.transferMoney(CARD_FROM_ID, CARD_TO_ID, AMOUNT);

        verify(cardFrom).setBalance(EXPECTED_BALANCE_FROM);
        verify(cardTo).setBalance(EXPECTED_BALANCE_TO);

        verify(cardRepository).findByIdWithLock(CARD_FROM_ID);
        verify(cardRepository).findByIdWithLock(CARD_TO_ID);
    }


    @Test
    void TransferMoneyService_Transfer_Not_Enough_Money() {
        CardEntity cardFrom = mock(CardEntity.class);
        CardEntity cardTo = mock(CardEntity.class);

        LocalDate futureDate = LocalDate.now().plusYears(1);

        when(cardFrom.getBalance()).thenReturn(BALANCE_FROM_NOT_ENOUGH);
        when(cardFrom.getStatus()).thenReturn(Status.ACTIVE);
        when(cardFrom.getExpiredDate()).thenReturn(futureDate);

        when(cardTo.getStatus()).thenReturn(Status.ACTIVE);
        when(cardTo.getExpiredDate()).thenReturn(futureDate);

        when(cardRepository.findByIdWithLock(CARD_FROM_ID)).thenReturn(Optional.of(cardFrom));
        when(cardRepository.findByIdWithLock(CARD_TO_ID)).thenReturn(Optional.of(cardTo));

        Assertions.assertThrows(CardBalanceException.class, () -> transferMoneyService.transferMoney(CARD_FROM_ID, CARD_TO_ID, AMOUNT));

        verify(cardFrom, never()).setBalance(any());
        verify(cardTo, never()).setBalance(any());
        verify(cardRepository).findByIdWithLock(CARD_FROM_ID);
        verify(cardRepository).findByIdWithLock(CARD_TO_ID);
    }


    @Test
    void TransferMoneyService_Transfer_Card_Is_Not_Active() {
        CardEntity cardFrom = mock(CardEntity.class);
        CardEntity cardTo = mock(CardEntity.class);

        when(cardFrom.getStatus()).thenReturn(Status.BLOCKED);

        when(cardRepository.findByIdWithLock(CARD_FROM_ID)).thenReturn(Optional.of(cardFrom));
        when(cardRepository.findByIdWithLock(CARD_TO_ID)).thenReturn(Optional.of(cardTo));

        Assertions.assertThrows(CardExpiredException.class, () -> transferMoneyService.transferMoney(CARD_FROM_ID, CARD_TO_ID, AMOUNT));

        verify(cardFrom, never()).setBalance(any());
        verify(cardTo, never()).setBalance(any());

        verify(cardRepository).findByIdWithLock(CARD_FROM_ID);
        verify(cardRepository).findByIdWithLock(CARD_TO_ID);
    }

    @Test
    void TransferMoneyService_Transfer_Card_Is_Expired() {
        CardEntity cardFrom = mock(CardEntity.class);
        CardEntity cardTo = mock(CardEntity.class);

        LocalDate expiredDate = LocalDate.of(2025, 9, 12);

        when(cardFrom.getStatus()).thenReturn(Status.ACTIVE);
        when(cardFrom.getExpiredDate()).thenReturn(expiredDate);

        when(cardTo.getStatus()).thenReturn(Status.ACTIVE);

        when(cardRepository.findByIdWithLock(CARD_FROM_ID)).thenReturn(Optional.of(cardFrom));
        when(cardRepository.findByIdWithLock(CARD_TO_ID)).thenReturn(Optional.of(cardTo));

        Assertions.assertThrows(CardExpiredException.class, () -> transferMoneyService.transferMoney(CARD_FROM_ID, CARD_TO_ID, AMOUNT));

        verify(cardFrom, never()).setBalance(any());
        verify(cardTo, never()).setBalance(any());
        verify(cardRepository).findByIdWithLock(CARD_FROM_ID);
        verify(cardRepository).findByIdWithLock(CARD_TO_ID);
    }
}