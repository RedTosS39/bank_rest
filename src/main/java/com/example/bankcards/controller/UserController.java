package com.example.bankcards.controller;


import com.example.bankcards.dto.CardDto;
import com.example.bankcards.security.PersonDetails;
import com.example.bankcards.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@ResponseBody
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/cards")
    public ResponseEntity<List<CardDto>> showMyCards(
            @AuthenticationPrincipal PersonDetails personDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(value = "searchingCard", required = false) String searchingCard) {

        Long currentUserId = personDetails.getUser().getId();
        Pageable pageable = PageRequest.of(page, size);
        Page<CardDto> dtoPage = userService.showUserCards(currentUserId, pageable, searchingCard);
        return ResponseEntity.ok(dtoPage.getContent());
    }

    @GetMapping("/balance")
    public ResponseEntity<CardDto> showMyBalance(
            @AuthenticationPrincipal PersonDetails personDetails,
            @RequestParam("cardId") Long cardId) {

        log.info("Current user ID: {}", personDetails.getUser().getId());
        log.info("Requested card ID: {}", cardId);

        Long currentUserId = personDetails.getUser().getId();
        CardDto cardDto = userService.showBalance(currentUserId, cardId);
        return ResponseEntity.ok(cardDto);
    }
}
