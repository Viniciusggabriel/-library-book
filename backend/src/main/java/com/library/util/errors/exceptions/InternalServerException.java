package com.library.util.errors.exceptions;

import org.eclipse.jetty.http.HttpStatus;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message) {
        super(String.format("message: %s, status: %d", message, HttpStatus.INTERNAL_SERVER_ERROR_500));
    }
}
