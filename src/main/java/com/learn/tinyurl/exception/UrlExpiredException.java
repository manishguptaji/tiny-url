package com.learn.tinyurl.exception;

public class UrlExpiredException extends RuntimeException {
    public UrlExpiredException(String s) {
        super(s);
    }
}
