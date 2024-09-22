package com.library.util.errors.exceptions;

public class ValueIsPresentInDatabase extends RuntimeException {
    public ValueIsPresentInDatabase(String message) {
        super(message);
    }

    public ValueIsPresentInDatabase(String message, Integer httpStatus) {
        super(String.format("message: %s, status: %d", message, httpStatus));
    }
}
