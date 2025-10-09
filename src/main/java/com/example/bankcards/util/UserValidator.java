package com.example.bankcards.util;

import com.example.bankcards.dto.UserDto;
import com.example.bankcards.exception.UserAlreadyExistException;
import com.example.bankcards.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class UserValidator implements Validator {

    private final AdminService adminService;

    @Autowired
    public UserValidator(AdminService adminService) {
        this.adminService = adminService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;
        String username = userDto.getUsername();

        log.info("Validating username: {}", username);

        try {
            adminService.loadUserByUsername(username);
            errors.rejectValue("username", "", username + " is already exist");
            log.error("username {} is already exist", username);
            throw new UserAlreadyExistException("username " + username + " is already exist");
        } catch (UsernameNotFoundException e) {
            log.info("OK: username {} not found", username);
        }
    }
}
