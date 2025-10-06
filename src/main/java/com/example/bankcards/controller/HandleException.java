package com.example.bankcards.controller;

import com.example.bankcards.exception.UserAlreadyExistException;
import com.example.bankcards.util.DefaultErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandleException {

    private ResponseEntity<DefaultErrorResponse> getResponse(RuntimeException error, String entityName) {
        DefaultErrorResponse response =
                new DefaultErrorResponse(System.currentTimeMillis(),
                        error.getMessage() + " " + entityName);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    private ResponseEntity<DefaultErrorResponse> handleException(UserAlreadyExistException exception) {
        return getResponse(exception, "User");
    }
}
