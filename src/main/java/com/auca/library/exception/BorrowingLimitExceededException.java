package com.auca.library.exception;

public class BorrowingLimitExceededException
        extends BorrowLimitExceededException {

    public BorrowingLimitExceededException(String message) {
        super(message);
    }
}