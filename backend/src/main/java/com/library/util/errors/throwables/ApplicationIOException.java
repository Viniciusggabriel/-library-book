package com.library.util.errors.throwables;

import lombok.Getter;

import java.io.IOException;

@Getter
public class ApplicationIOException extends IOException {
    private final int statusCode;

    public ApplicationIOException(String message, Integer httpStatus) {
        super(message);
        this.statusCode = httpStatus;
    }
}
