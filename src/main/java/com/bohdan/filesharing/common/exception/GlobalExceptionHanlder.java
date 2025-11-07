package com.bohdan.filesharing.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHanlder {

    @ExceptionHandler(UserAuthenticationException.class)
    public ResponseEntity<String> handleUserUsernameException(UserAuthenticationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

    }
}
