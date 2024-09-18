package com.library.util.errors.exceptions;

/**
 * Exception para porta de servidor http com erros
 */
public class PortHttpServerException extends RuntimeException {
    public PortHttpServerException(String message) {
        super(message);
    }

    public PortHttpServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
