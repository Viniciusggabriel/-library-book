package com.library.util.errors.throwables;

import lombok.Getter;

@Getter
public class ApplicationRuntimeException extends RuntimeException {
    private final int statusCode;

    public ApplicationRuntimeException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
