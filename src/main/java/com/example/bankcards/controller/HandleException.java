package com.example.bankcards.controller;

import com.example.bankcards.exception.*;
import com.example.bankcards.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class HandleException {

    private ResponseEntity<ErrorResponse> getResponse(RuntimeException exception, HttpStatus status) {
        ErrorResponse response =
                new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        LocalDateTime.now().toString(),
                        exception.getMessage());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler({CardNotFoundException.class, CardNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException exception) {
        return getResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({CardAlreadyExistException.class, UserAlreadyExistException.class, CardAlreadyAssignException.class})
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExistException(RuntimeException exception) {
        return getResponse(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({CardExpiredException.class, CardBalanceException.class})
    public ResponseEntity<ErrorResponse> handleCardException(CardExpiredException exception) {
        return getResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CardNumberException.class)
    public ResponseEntity<ErrorResponse> handleCardException(CardNumberException exception) {
        return getResponse(exception, HttpStatus.BAD_REQUEST);
    }
}
