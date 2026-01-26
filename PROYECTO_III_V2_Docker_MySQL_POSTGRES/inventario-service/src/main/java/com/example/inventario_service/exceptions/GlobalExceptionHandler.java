package com.example.inventario_service.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return new ApiError(Instant.now().toString(), HttpStatus.BAD_REQUEST.value(), "Validation Error", errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        ApiError api = new ApiError(Instant.now().toString(), HttpStatus.NOT_FOUND.value(), ex.getMessage(), Map.of());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(api);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiError> handleInvalidOp(InvalidOperationException ex) {
        ApiError api = new ApiError(Instant.now().toString(), HttpStatus.BAD_REQUEST.value(), ex.getMessage(), Map.of());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(api);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex) {
        ApiError api = new ApiError(Instant.now().toString(), HttpStatus.CONFLICT.value(), "Database constraint violation", Map.of());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(api);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex) {
        ApiError api = new ApiError(Instant.now().toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", Map.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(api);
    }
}
