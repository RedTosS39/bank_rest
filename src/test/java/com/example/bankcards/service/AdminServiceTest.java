package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.CardEntity;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.CardAlreadyAssignException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AdminServiceTest {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "testPassword";
    private static final Long USER_ID = 1L;
    private static final Long CARD_ID = 1L;
    private static final String CARD_NUMBER = "1234123412341234";

    private ModelMapper modelMapper;

    @MockitoBean
    private PeopleRepository peopleRepository;
    @MockitoBean
    private CardRepository cardRepository;

    @Autowired
    private AdminService adminService;


    @Test
    void AdminService_Try_To_assign_If_Card_Already_Uses() {
        UserEntity user = createUser();
        CardEntity card = createCard();

        when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(card));
        when(peopleRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        when(card.getUserEntity()).thenReturn(user);

        adminService.assignCardToUser(USER_ID, CARD_ID);

    }

    @Test
    void AdminService_Assign_Card_To_User_Without_Card() {
        UserEntity user = createUser();
        CardEntity card = createCard();

        when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(card));
        when(peopleRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        adminService.assignCardToUser(USER_ID, CARD_ID);
        assertTrue(user.getCards().contains(card));
        assertEquals(card.getUserEntity(), user);

        verify(peopleRepository).findById(USER_ID);
        verify(cardRepository).findById(CARD_ID);
    }


    @Test
    void AdminService_Try_Assign_If_Card_Already_Assigned() {
        UserEntity user = mock(UserEntity.class);
        CardEntity card = mock(CardEntity.class);

        when(cardRepository.findById(CARD_ID)).thenReturn(Optional.of(card));
        when(peopleRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(card.getUserEntity()).thenReturn(user);

        assertThrows(CardAlreadyAssignException.class, () -> adminService.assignCardToUser(USER_ID, USER_ID));
    }


    @Test
    void AdminService_Try_To_Load_User_By_Username_If_Not_Exist() {
        UserEntity userEntity = mock(UserEntity.class);
        when(userEntity.getUsername()).thenReturn(USERNAME);
        when(peopleRepository.findByUsername(userEntity.getUsername())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> adminService.loadUserByUsername(userEntity.getUsername()));
    }

    @Test
    void AdminService_load_User_By_Username_If_Exist() {
        UserDto expected = modelMapper.map(createUser(), UserDto.class);
        UserDto userDto = createUserDto();
        when(peopleRepository.findById(expected.getId())).thenReturn(null);
        assertEquals(userDto, expected);
    }


    private CardEntity createCard() {
        return CardEntity.builder()
                .id(CARD_ID)
                .expiredDate(LocalDate.now().plusDays(1))
                .status(Status.ACTIVE)
                .cardNumber(CARD_NUMBER)
                .userEntity(null)
                .build();
    }

    private UserEntity createUser() {
        return UserEntity.builder()
                .id(1L)
                .username(USERNAME)
                .password(PASSWORD)
                .cards(new ArrayList<>())
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