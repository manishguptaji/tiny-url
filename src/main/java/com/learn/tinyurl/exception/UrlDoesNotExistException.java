package com.learn.tinyurl.exception;

public class UrlDoesNotExistException extends RuntimeException {
    public UrlDoesNotExistException(String s) {
        super(s);
    }
}
