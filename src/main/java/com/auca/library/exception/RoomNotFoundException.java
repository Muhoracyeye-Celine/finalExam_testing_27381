package com.auca.library.exception;

public class RoomNotFoundException
        extends RuntimeException {

    public RoomNotFoundException(String message) {
        super(message);
    }
}