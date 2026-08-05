package com.auca.library.exception;

public class UserNotFoundException
        extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}