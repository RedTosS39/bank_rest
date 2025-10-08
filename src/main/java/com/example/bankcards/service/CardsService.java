package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.CardAlreadyExistException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.PeopleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CardsService {

    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;
    private final PeopleRepository peopleRepository;

    @Autowired
    public CardsService(CardRepository cardRepository, ModelMapper modelMapper, PeopleRepository peopleRepository) {
        this.cardRepository = cardRepository;
        this.modelMapper = modelMapper;
        this.peopleRepository = peopleRepository;
    }

    @Transactional
    public void createCard(CardDto cardDto) {
        CardEntity cardEntity = modelMapper.map(cardDto, CardEntity.class);
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

    private CardEntity getCardEntity(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException("Couldnt find card with id: " + cardId));
    }

    public List<CardDto> showCards() {
        return cardRepository.findAll()
                .stream()
                .map(cardEntity -> modelMapper.map(cardEntity, CardDto.class))
                .toList();
    }
}
