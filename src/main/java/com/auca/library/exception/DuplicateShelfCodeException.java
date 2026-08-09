package com.auca.library.exception;

public class DuplicateShelfCodeException
        extends RuntimeException {

    public DuplicateShelfCodeException(String message) {
        super(message);
    }
}