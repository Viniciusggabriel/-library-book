package com.library.util.errors.exceptions;

public class InternalServerException extends RuntimeException {
    public InternalServerException() {
        super("Erro interno no servidor!");
    }

    public InternalServerException(String message) {
        super(message);
    }
}
