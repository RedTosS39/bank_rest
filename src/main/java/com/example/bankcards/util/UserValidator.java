package com.example.bankcards.util;

import com.example.bankcards.entity.UserEntity;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final PeopleService peopleService;

    @Autowired
    public UserValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserEntity.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        try {
            peopleService.loadUserByUsername((String) target);
        } catch (UserNotFoundException ignored) {
            return;  //все ок, user не найден.
        }
        errors.rejectValue("username", "", "Username is already taken");
    }
}
