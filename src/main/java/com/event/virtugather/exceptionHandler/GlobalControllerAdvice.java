package com.event.virtugather.exceptionHandler;

import com.event.virtugather.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> handleDuplicationKeyException(DuplicateKeyException ex) {
        log.error("Duplicate key exception occurred: {}", ex.getMessage());
        String userFriendlyMessage = "A duplicate record was attempted to be created. Please check your input.";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(userFriendlyMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>("Required request body is missing, due to "+ ex.getMessage() , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException e) {

        // Log the exception; consider using a logging library such as slf4j
        System.err.println("An IOException occurred: " + e.getMessage());

        // Return a ResponseEntity with a status of INTERNAL_SERVER_ERROR
        return new ResponseEntity<>("An error occurred while processing the request", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException e) {
        System.err.println("A NotFoundException occurred: " + e.getMessage());
        return new ResponseEntity<>("The requested resource could not be found", HttpStatus.NOT_FOUND);
    }
}