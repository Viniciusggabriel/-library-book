package com.library.util.errors.exceptions;

import com.library.util.errors.throwables.ApplicationRuntimeException;
import lombok.Getter;

@Getter
public class InvalidRequestPathParameterException extends ApplicationRuntimeException {
 /**
     * <h3>Exception que extended a principal e lança os erros necessários</h3>
     *
     * @param message    -> <strong>Message de erro</strong>
     * @param httpStatus -> <strong>Código de erro</strong>
     */
    public InvalidRequestPathParameterException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }

}
