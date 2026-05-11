package com.giovanna.saas_billings.handler;

import com.giovanna.saas_billings.dto.CustomResponseDto;
import com.giovanna.saas_billings.exception.InvalidOperationException;
import com.giovanna.saas_billings.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomResponseDto> handleNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), ex.getMessage(), LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<CustomResponseDto> handleBadRequestException(InvalidOperationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomResponseDto(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), ex.getMessage(), LocalDateTime.now()));
    }
}
