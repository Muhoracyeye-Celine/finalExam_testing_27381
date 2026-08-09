package com.auca.library.exception;

public class DuplicateBookCodeException
        extends RuntimeException {

    public DuplicateBookCodeException(String message) {
        super(message);
    }
}