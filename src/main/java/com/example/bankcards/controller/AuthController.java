package com.example.bankcards.controller;

import com.example.bankcards.service.JwtService;
import com.example.bankcards.dto.AuthRequest;
import com.example.bankcards.dto.JwtResponse;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.service.RegistrationService;
import com.example.bankcards.service.ShowErrorMessage;
import com.example.bankcards.util.UserValidator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public AuthController(RegistrationService registrationService,
                          UserValidator userValidator,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @GetMapping()
    public ResponseEntity<HttpStatus> getRegistration() {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/registration")
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .map(auth -> auth.replace("ROLE_", ""))
                    .orElse("USER");

            String jwt = jwtService.generateToken(userDetails.getUsername(), role);

            JwtResponse response = new JwtResponse(jwt, "Bearer", userDetails.getUsername(), role);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }
}