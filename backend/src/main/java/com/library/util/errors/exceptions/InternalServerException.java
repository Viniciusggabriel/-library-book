package com.library.util.errors.exceptions;

/**
 * Classe de exception personalizada para erros internos no servidor
 */
public class InternalServerException extends RuntimeException {
    public InternalServerException() {
        super("Erro interno no servidor!");
    }

    public InternalServerException(String message) {
        super(message);
    }
}
