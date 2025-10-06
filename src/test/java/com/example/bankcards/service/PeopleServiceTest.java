package com.example.bankcards.service;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.repository.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PeopleServiceTest {

    private ModelMapper modelMapper;
    @MockitoBean
    private PeopleRepository peopleRepository;

    @Autowired
    private PeopleService peopleService;

    @Test
    void PeopleService_load_User_By_Username_If_Not_Exist() {
        UserDto expected = modelMapper.map(createUser(), UserDto.class);
        UserDto userDto = createUserDto();
        when(peopleRepository.findById(expected.getId())).thenReturn(null);
        assertEquals(userDto, expected);
    }

    @Test
    void PeopleService_load_User_By_Username() {
        //TODO
    }


    private UserEntity createUser() {
        return UserEntity.builder()
                .id(1L)
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