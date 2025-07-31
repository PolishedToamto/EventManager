package com.myDomain.eventManager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getParameterValidationResults().forEach(result ->
                result.getResolvableErrors().forEach(error -> {
                    String field = result.getMethodParameter().getParameterName();
                    String message = error.getDefaultMessage();
                    errors.put(field, message);
                })
        );
        return ResponseEntity.badRequest().body(errors);
    }
}
