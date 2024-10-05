package com.library.util.errors.exceptions;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;

@Getter
public class MalformedJsonException extends JsonProcessingException {
    private final int statusCode;


    /**
     * <h3>Exception que extended a principal e lança os erros necessários</h3>
     *
     * @param message    -> <strong>Message de erro</strong>
     * @param httpStatus -> <strong>Código de erro</strong>
     */
    public MalformedJsonException(String message, Integer httpStatus) {
        super(message);
        this.statusCode = httpStatus;
    }

    /**
     * <h3>Exception que extended a principal e lança os erros necessários</h3>
     *
     * @param message    -> <strong>Message de erro</strong>
     * @param loc        -> <strong>Localização de onde está o erro do json</strong>
     * @param httpStatus -> <strong>Código de erro</strong>
     */
    public MalformedJsonException(String message, JsonLocation loc, Integer httpStatus) {
        super(message, loc);
        this.statusCode = httpStatus;
    }
}
