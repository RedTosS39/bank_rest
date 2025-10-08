package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.CardAlreadyExistException;
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
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CardsServiceTest {

    private static final String CARD_NUMBER = "123456789123456";
    private static final Long CARD_ID = 1L;
    private static final BigDecimal CARD_BALANCE = BigDecimal.valueOf(0);
    private static final LocalDate EXPIRED_CARD_DATE = LocalDate.of(2026, Month.FEBRUARY, 22);

    @MockitoBean
    private CardRepository cardRepository;

    @Autowired
    private CardsService cardsService;

    @Test
    void CardsService_Create_Card_If_Not_Exist() {
        CardEntity cardEntity = cardEntity();
        CardDto cardDto = cardDto();
        when(cardRepository.findById(CARD_ID)).thenReturn(Optional.empty());
        when(cardRepository.save(any(CardEntity.class))).thenReturn(cardEntity);
        cardsService.createCard(cardDto);
        verify(cardRepository).findByCardNumber(cardDto.getCardNumber());
        verify(cardRepository).save(any(CardEntity.class));
    }


    @Test
    void CardsService_Create_Card_If_Exist() {
        CardDto cardDto = cardDto();
        when(cardRepository.findByCardNumber(CARD_NUMBER)).thenThrow(CardAlreadyExistException.class);
        assertThrows(CardAlreadyExistException.class, () -> cardsService.createCard(cardDto));
    }

    @Test
    void CardsService_Block_Card_If_Exist() {
        CardEntity cardEntity = cardEntity();

        when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(cardEntity));
        cardEntity.setStatus(Status.BLOCKED);
        when(cardRepository.save(any(CardEntity.class))).thenReturn(cardEntity);

        cardsService.changeStatus(CARD_ID, Status.BLOCKED);

        verify(cardRepository).findById(CARD_ID);
        verify(cardRepository).save(cardEntity);
        Assertions.assertEquals(Status.BLOCKED, cardEntity.getStatus());
    }


    @Test
    void CardService_Delete_Card() {
        UserEntity userEntity = new UserEntity();
        CardEntity cardEntity = cardEntity();

        userEntity.setCards(new ArrayList<>(List.of(cardEntity)));
        cardEntity.setUserEntity(userEntity);

        when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(cardEntity));

        cardsService.deleteCard(CARD_ID);

        verify(cardRepository).findById(CARD_ID);
        assertNull(cardEntity.getUserEntity());
        assertFalse(userEntity.getCards().contains(cardEntity));
        verify(cardRepository).delete(cardEntity);
    }


    private CardEntity cardEntity() {
        return CardEntity.builder()
                .id(CARD_ID)
                .cardNumber(CARD_NUMBER)
                .userEntity(new UserEntity())
                .balance(CARD_BALANCE)
                .status(Status.ACTIVE)
                .expiredDate(EXPIRED_CARD_DATE)
                .build();
    }

    private CardDto cardDto() {

        return CardDto.builder()
                .id(CARD_ID)
                .cardNumber(CARD_NUMBER)
                .balance(CARD_BALANCE)
                .status(Status.ACTIVE)
                .expiredDate(EXPIRED_CARD_DATE)
                .build();
    }
}