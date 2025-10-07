package com.example.bankcards.service;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public interface ShowErrorMessage {

    static String showErrorMessage(BindingResult bindingResult) {

        StringBuilder errorsMsg = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorsMsg.append(fieldError.getField())
                    .append(" : ")
                    .append(fieldError.getDefaultMessage())
                    .append(";");
        }
        return errorsMsg.toString();
    }
}
