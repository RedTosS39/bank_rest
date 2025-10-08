package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.security.PersonDetails;
import com.example.bankcards.service.AdminService;
import com.example.bankcards.service.CardsService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@ResponseBody
@RequestMapping("/admin")
public class AdminController {

    ///  - Создает V, блокирует V, активирует V, удаляет карты V
    ///  - Управляет пользователями ?
    ///  - Видит все карты V

    private final AdminService adminService;
    private final ModelMapper modelMapper;
    private final CardsService cardsService;


    @Autowired
    public AdminController(AdminService adminService, ModelMapper modelMapper, CardsService cardsService) {
        this.adminService = adminService;
        this.modelMapper = modelMapper;
        this.cardsService = cardsService;
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUsers(@AuthenticationPrincipal PersonDetails personDetails) {
        Map<String, Object> response = new HashMap<>();

        UserEntity user = personDetails.getUser();
        if (user != null) {
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .cards(user.getCards()
                            .stream()
                            .map(it -> modelMapper.map(it, CardDto.class))
                            .collect(Collectors.toList()))
                    .build();

            response.put("type", "PersonDetails");
            response.put("username", userDto.getUsername());
            response.put("user", userDto);
            log.info("User details: {}", userDto);
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            response.put("type", "Unknown");
            response.put("username", authentication.getName());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        response.put("authenticated", authentication.isAuthenticated());
        response.put("authorities", authentication.getAuthorities());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cards")
    public ResponseEntity<HttpStatus> showCards() {
        cardsService.showCards();
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/create_card")
    public ResponseEntity<HttpStatus> createCard(@RequestBody CardDto cardDto) {
        cardsService.createCard(cardDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}/block")
    public ResponseEntity<HttpStatus> changeStatus(@PathVariable("id") Long cardId,
                                                   @RequestParam("status") Status status) {
        cardsService.changeStatus(cardId, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}/cards")
    public ResponseEntity<HttpStatus> deleteCard(@PathVariable("id") Long cardId) {
        cardsService.deleteCard(cardId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(HttpStatus.OK);
    }


    @GetMapping("/{id}/user")
    public ResponseEntity<HttpStatus> showUser(@PathVariable Long id) {
        adminService.findUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
