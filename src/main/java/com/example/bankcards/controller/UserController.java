package com.example.bankcards.controller;


import com.example.bankcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ResponseBody
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<HttpStatus> showUserCards(@PathVariable("id") Long id,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size,
                                                    @RequestParam("search") String search) {
        Pageable pageable = PageRequest.of(page, size);
        userService.showUserCards(id, pageable, search);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/{id}/balance")
    public ResponseEntity<HttpStatus> showBalance(@PathVariable("id") Long userId,
                                                  @RequestParam("cardId") Long cardId) {
        userService.showBalance(userId, cardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
