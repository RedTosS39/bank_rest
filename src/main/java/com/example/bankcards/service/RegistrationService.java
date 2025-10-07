package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.UserAlreadyExistException;
import com.example.bankcards.repository.PeopleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Service
public class RegistrationService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerNewUser(UserDto userDto) {
        String password = passwordEncoder.encode(userDto.getPassword());
        UserEntity newUser = UserEntity.builder()
                .username(userDto.getUsername())
                .password(password)
                .role(userDto.getRole())
                .cards(new ArrayList<>())
                .build();

        log.info("user created{}{}{}", newUser.getUsername(), newUser.getPassword(), newUser.getRole());
        if (peopleRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username already exist");
        }

        peopleRepository.save(newUser);
    }
}
