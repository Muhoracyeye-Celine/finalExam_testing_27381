package com.auca.library.exception;

public class DuplicateUsernameException
        extends RuntimeException {

    public DuplicateUsernameException(String message) {
        super(message);
    }
}