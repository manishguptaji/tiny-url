package com.learn.tinyurl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AliasNotAvailableException extends RuntimeException {
    public AliasNotAvailableException(String message) {
        super(message);
    }
}
