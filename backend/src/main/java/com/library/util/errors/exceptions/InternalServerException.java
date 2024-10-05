package com.library.util.errors.exceptions;

import com.library.util.errors.throwables.ApplicationRuntimeException;
import org.eclipse.jetty.http.HttpStatus;

public class InternalServerException extends ApplicationRuntimeException {
    /**
     * <h3>Exception que extended a principal e lança os erros necessários</h3>
     *
     * @param message -> <strong>Message de erro</strong>
     */
    public InternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR_500);
    }
}
