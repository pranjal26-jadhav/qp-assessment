package com.grocery.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class HttpException extends ResponseStatusException {
    public HttpException(HttpStatus status, String reason) {
        super(status, reason);
    }
}
