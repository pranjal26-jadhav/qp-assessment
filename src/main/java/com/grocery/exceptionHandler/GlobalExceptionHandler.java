package com.grocery.exceptionHandler;

import com.grocery.dtos.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Response> handleException(HttpException ex) {
        Response response = Response.builder().message(ex.getMessage()).build();
        return ResponseEntity.status(ex.getStatus()).body(response);
    }
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Response> handleDataLevelException(DataAccessException ex) {
        Response response = Response.builder().message(ex.getMessage()).build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Need to hide db error and translate to user friendly message
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handlerException(Exception ex) {
        Response response = Response.builder().message("Please contact support team").build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // manage with Id
    }
}
