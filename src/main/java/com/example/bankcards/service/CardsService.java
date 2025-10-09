package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.Status;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class CardsService {

    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CardsService(CardRepository cardRepository, ModelMapper modelMapper) {
        this.cardRepository = cardRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public CardDto createCard() {
        CardEntity cardEntity = new CardEntity();
        cardEntity.setCardNumber(generateUniqueCardNumber());
        cardRepository.save(cardEntity);
        return modelMapper.map(cardEntity, CardDto.class);
    }

    @Transactional
    public void changeStatus(Long cardId, Status status) {
        CardEntity cardEntity = getCardEntity(cardId);
        cardEntity.setStatus(status);
        cardRepository.save(cardEntity);
    }

    @Transactional
    public void deleteCard(Long cardId) {
        CardEntity cardEntity = getCardEntity(cardId);
        if (cardEntity.getUserEntity() != null) {
            cardEntity.getUserEntity().getCards().remove(cardEntity);
            cardEntity.setUserEntity(null);
        }
        cardRepository.delete(cardEntity);
    }

    public List<CardDto> showCards() {
        return cardRepository.findAll()
                .stream()
                .map(cardEntity -> modelMapper.map(cardEntity, CardDto.class))
                .toList();
    }

    private CardEntity getCardEntity(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException("Couldnt find card with id: " + cardId));
    }


    private String generateUniqueCardNumber() {
        String cardNumber;
        do {
            cardNumber = generateCardNumber();
        } while (cardRepository.findByCardNumber(cardNumber).isPresent());

        return cardNumber;
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }
}
