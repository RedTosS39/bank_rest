package com.example.bankcards.controller;

import com.example.bankcards.security.PersonDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController

@RequestMapping("/users")
public class UsersController {

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
}
