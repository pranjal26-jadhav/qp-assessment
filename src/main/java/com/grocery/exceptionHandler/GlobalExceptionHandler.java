package com.grocery.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<String> handleException(HttpException ex) {
        log.info("inside http exception");
        return ResponseEntity.status(ex.getStatus()).body(ex.getMessage());
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataLevelException(DataAccessException e) {
        log.info("Inside data access exception");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // Need to hide db error and translate to user friendly message
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handlerException(Exception message) {
        log.info("Inside data access exception");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please contact support team"); // manage with Id
    }
}
