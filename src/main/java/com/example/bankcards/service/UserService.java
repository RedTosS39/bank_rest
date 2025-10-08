package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.PeopleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    /**
     * - Просматривает свои карты V (поиск + пагинация) V
     * - Запрашивает блокировку карты
     * - Делает переводы между своими картами
     * - Смотрит баланс
     */

    private final CardRepository cardRepository;
    private final PeopleRepository peopleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(CardRepository cardRepository, PeopleRepository peopleRepository, ModelMapper modelMapper) {
        this.cardRepository = cardRepository;
        this.peopleRepository = peopleRepository;
        this.modelMapper = modelMapper;
    }

    public Page<CardDto> showUserCards(Long userId, Pageable pages, String search) {
        peopleRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Page<CardEntity> entityPage = cardRepository.findByUserEntityIdAndCardNumberContaining(userId, search, pages);
        Page<CardDto> dtoPage = entityPage.map(card -> modelMapper.map(card, CardDto.class));
        return dtoPage;
    }
}
