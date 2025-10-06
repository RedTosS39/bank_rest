package com.example.bankcards.controller;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.exception.UserAlreadyExistException;
import com.example.bankcards.security.PersonDetails;
import com.example.bankcards.service.PeopleService;
import com.example.bankcards.service.PersonDetailsService;
import com.example.bankcards.service.RegistrationService;
import com.example.bankcards.service.ShowErrorMessage;
import com.example.bankcards.util.UserValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController

@RequestMapping("/users")
public class PeopleController {

    private ShowErrorMessage showErrorMessage;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final PeopleService peopleService;

    @Autowired
    public PeopleController(UserValidator userValidator, RegistrationService registrationService, PeopleService peopleService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.peopleService = peopleService;
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> response = new HashMap<>();

        Object principal = authentication.getPrincipal();

        if (principal instanceof PersonDetails personDetails) {
            response.put("type", "PersonDetails");
            response.put("username", personDetails.getUsername());
            response.put("user", personDetails.getUser());
            log.info("User details: {}", personDetails);
        } else if (principal instanceof String) {
            response.put("type", "String");
            response.put("username", principal);
            log.info("User as string: {}", principal);
        } else {
            response.put("type", "Unknown: " + principal.getClass().getName());
            response.put("principal", principal);
        }

        response.put("authenticated", authentication.isAuthenticated());
        response.put("authorities", authentication.getAuthorities());

        return ResponseEntity.ok(response);
    }


    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registerNewUser(@RequestBody @Valid UserDto userDto,
                                                      BindingResult bindingResult) {
        userValidator.validate(userDto, bindingResult);
        if (bindingResult.hasErrors()) {
            log.info(showErrorMessage.showErrorMessage(bindingResult));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        registrationService.registerNewUser(userDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<HttpStatus> showUser(@PathVariable Long id) {
        peopleService.findUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
