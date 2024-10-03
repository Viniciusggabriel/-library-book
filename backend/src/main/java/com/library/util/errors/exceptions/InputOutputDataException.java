package com.library.util.errors.exceptions;

import com.library.util.errors.throwables.ApplicationIOException;

public class InputOutputDataException extends ApplicationIOException {
    /**
     * <h3>Exception que serve para personalizar erros de IO</h3>
     *
     * @param message    -> <strong>Message de erro</strong>
     * @param httpStatus -> <strong>Código de erro</strong>
     */
    public InputOutputDataException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
