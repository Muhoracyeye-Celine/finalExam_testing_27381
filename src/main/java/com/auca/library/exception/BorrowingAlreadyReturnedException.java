package com.auca.library.exception;

public class BorrowingAlreadyReturnedException
        extends RuntimeException {

    public BorrowingAlreadyReturnedException(
            String message
    ) {
        super(message);
    }
}