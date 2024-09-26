package com.library.util.errors.exceptions;

public class ValueNotFound extends RuntimeException {
    public ValueNotFound(String message) {
        super(message);
    }

    public ValueNotFound(String message, Integer httpStatus) {
        super(String.format("message: %s, status: %d", message, httpStatus));
    }
}
