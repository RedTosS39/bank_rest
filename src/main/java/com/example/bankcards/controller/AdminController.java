package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Status;
import com.example.bankcards.service.AdminService;
import com.example.bankcards.service.CardsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@ResponseBody
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final CardsService cardsService;

    @Autowired
    public AdminController(AdminService adminService, CardsService cardsService) {
        this.adminService = adminService;
        this.cardsService = cardsService;
    }

    @GetMapping("/cards")
    public ResponseEntity<List<CardDto>> showCards() {
        List<CardDto> cardDtoList = cardsService.showCards();
        return ResponseEntity.ok(cardDtoList);
    }

    @GetMapping("/create_card")
    public ResponseEntity<CardDto> createCard() {
        CardDto cardDto = cardsService.createCard();
        return ResponseEntity.status(HttpStatus.CREATED).body(cardDto);
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
    public ResponseEntity<UserDto> showUser(@PathVariable Long id) {
        UserDto userDto = adminService.findUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<HttpStatus> assignCardToUser(@PathVariable("id") Long userId,
                                                       @RequestParam Long cardId) {
        adminService.assignCardToUser(userId, cardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
