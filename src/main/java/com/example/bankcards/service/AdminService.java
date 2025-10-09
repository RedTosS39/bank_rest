package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.CardAlreadyAssignException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.PeopleRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
public class AdminService {

    private final CardRepository cardRepository;
    private final PeopleRepository peopleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminService(CardRepository cardRepository, PeopleRepository peopleRepository, ModelMapper modelMapper) {
        this.cardRepository = cardRepository;
        this.peopleRepository = peopleRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void assignCardToUser(Long userId, Long cardId) {
        CardEntity cardEntity = cardRepository.findById(cardId).orElseThrow(() -> new CardNotFoundException("card with id: " + cardId + " not found"));
        UserEntity userEntity = peopleRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (userEntity.getCards().contains(cardEntity)) {
            throw new CardAlreadyAssignException("Card already assigned to user");
        }

        if (cardEntity.getUserEntity() != null) {
            throw new CardAlreadyAssignException("Card already used by someone else");
        }
        cardEntity.setExpiredDate(LocalDate.now());
        cardEntity.setStatus(Status.ACTIVE);
        cardEntity.setBalance(BigDecimal.valueOf(1000.0));

        userEntity.getCards().add(cardEntity);
        cardEntity.setUserEntity(userEntity);

        log.info("User cards after assign: {}", userEntity.getCards());
        log.info("username who have this card: {}", cardEntity.getUserEntity().getUsername());
    }


    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = peopleRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        log.info("User has been found: {}", user.getUsername());
        return modelMapper.map(user, UserDto.class);
    }

    public UserDto findUserById(Long id) throws UsernameNotFoundException {
        UserEntity user = peopleRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User with id:" + id + " not found"));
        log.info("User has been found{}", user.getId());
        return modelMapper.map(user, UserDto.class);
    }
}
