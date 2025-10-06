package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.UserAlreadyExistException;
import com.example.bankcards.repository.PeopleRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class RegistrationServiceTest {

    @MockitoBean
    private PeopleRepository peopleRepository;
    @Autowired
    private RegistrationService registrationService;

    @Test
    void RegistrationService_Register_New_User_If_Not_Exist() {
        UserEntity user = createUser();
        UserDto userDto = createUserDto();
        when(peopleRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(peopleRepository.save(any(UserEntity.class))).thenReturn(user);
        registrationService.registerNewUser(userDto);
        verify(peopleRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void RegistrationService_Register_New_User_If_Exist() {
        UserEntity user = createUser();
        UserDto userDto = createUserDto();
        when(peopleRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        assertThrows(UserAlreadyExistException.class, () -> registrationService.registerNewUser(userDto));
        verify(peopleRepository, times(1)).findByUsername(userDto.getUsername());
        verify(peopleRepository, never()).save(any(UserEntity.class));
    }


    private UserEntity createUser() {
        return UserEntity.builder()
                .username("testUser")
                .password("testPassword")
                .cards(Collections.emptyList())
                .build();
    }

    private UserDto createUserDto() {
        return UserDto.builder()
                .cards(Collections.emptyList())
                .username("testUser")
                .password("testPassword")
                .role(Role.USER)
                .build();
    }
}