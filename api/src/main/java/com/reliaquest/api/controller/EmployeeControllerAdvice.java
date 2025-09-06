package com.reliaquest.api.controller;

import com.reliaquest.api.exception.EmployeeAPIException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@ControllerAdvice
public class EmployeeControllerAdvice {
    @ExceptionHandler
    protected ResponseEntity<?> handleTooManyException(HttpClientErrorException.TooManyRequests ex) {
        log.error("Error handling web request.", ex);
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too Many Requests. Please try in some time");
    }

    @ExceptionHandler
    protected ResponseEntity<?> handleNotFoundException(HttpClientErrorException.NotFound ex) {
        log.error("Error handling web request.", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler
    protected ResponseEntity<?> handleException(Throwable ex) {
        log.error("Error handling web request.", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
    }

    @ExceptionHandler
    protected ResponseEntity<?> handleEmployeeAPIException(EmployeeAPIException ex) {
        log.error("Error occurred for employee operation", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
