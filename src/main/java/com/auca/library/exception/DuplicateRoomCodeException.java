package com.auca.library.exception;

public class DuplicateRoomCodeException
        extends RuntimeException {

    public DuplicateRoomCodeException(String message) {
        super(message);
    }
}