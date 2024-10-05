package com.library.util.errors.exceptions;

import com.library.util.errors.throwables.ApplicationServletException;

public class ErrorMakingRequestException extends ApplicationServletException {
    /**
     * <h3>Exception que extended a principal e lança os erros necessários</h3>
     *
     * @param message    -> <strong>Message de erro</strong>
     * @param httpStatus -> <strong>Código de erro</strong>
     */
    public ErrorMakingRequestException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
