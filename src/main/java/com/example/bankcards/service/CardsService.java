package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.Status;
import com.example.bankcards.exception.CardAlreadyExistException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.CardNumberException;
import com.example.bankcards.repository.CardRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public void createCard(CardDto cardDto) {
        CardEntity cardEntity = modelMapper.map(cardDto, CardEntity.class);
        String checkedCardNumber = getCheckedCardNumber(cardDto);
        cardEntity.setCardNumber(checkedCardNumber);

        if (cardRepository.findByCardNumber(cardEntity.getCardNumber()).isPresent()) {
            throw new CardAlreadyExistException("Card already exist");
        }
        cardRepository.save(cardEntity);
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

    private String getCheckedCardNumber(CardDto cardDto) {
        String checkedNumber = cardDto.getCardNumber().replaceAll("\\D", "");

        if (!checkedNumber.matches("\\d{16}")) {
            throw new CardNumberException("Must be only digits");
        }

        if (checkedNumber.length() != 16) {
            throw new CardNumberException("there are not 16 digits");
        }
        return checkedNumber;
    }
}
