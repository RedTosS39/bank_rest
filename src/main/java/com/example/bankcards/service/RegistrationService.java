package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.UserAlreadyExistException;
import com.example.bankcards.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class RegistrationService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Transactional
    public void registerNewUser(UserDto userDto) {
        UserEntity newUser = UserEntity.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .role(userDto.getRole())
                .cards(new ArrayList<>())
                .build();

        if (peopleRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username already exist");
        }

        peopleRepository.save(newUser);
    }
}
