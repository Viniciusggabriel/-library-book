package com.library.util.errors.exceptions;

import com.library.util.errors.throwables.ApplicationRuntimeException;

public class ValueNotFoundException extends ApplicationRuntimeException {

    /**
     * <h3>Exception que extended a principal e lança os erros necessários</h3>
     *
     * @param message    -> <strong>Message de erro</strong>
     * @param httpStatus -> <strong>Código de erro</strong>
     */
    public ValueNotFoundException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
