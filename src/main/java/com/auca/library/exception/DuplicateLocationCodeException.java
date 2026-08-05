package com.auca.library.exception;

public class DuplicateLocationCodeException
        extends RuntimeException {

    public DuplicateLocationCodeException(String message) {
        super(message);
    }
}