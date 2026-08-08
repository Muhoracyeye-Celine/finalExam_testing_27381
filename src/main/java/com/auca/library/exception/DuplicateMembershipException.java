package com.auca.library.exception;

public class DuplicateMembershipException
        extends RuntimeException {

    public DuplicateMembershipException(String message) {
        super(message);
    }
}