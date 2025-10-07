package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.service.RegistrationService;
import com.example.bankcards.service.ShowErrorMessage;
import com.example.bankcards.util.UserValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@ResponseBody
@RequestMapping("/registration")
public class AuthController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;

    @Autowired
    public AuthController(RegistrationService registrationService, UserValidator userValidator) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
    }

    @GetMapping()
    public ResponseEntity<HttpStatus> getRegistration() {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> registerNewUser(@RequestBody @Valid UserDto userDto,
                                                      BindingResult bindingResult) {
        userValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info(ShowErrorMessage.showErrorMessage(bindingResult));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.info("user created{}{}{}", userDto.getUsername(), userDto.getPassword(), userDto.getRole());

        registrationService.registerNewUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
