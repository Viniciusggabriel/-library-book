package com.library.util.errors.exceptions;

import com.library.util.errors.throwables.ApplicationServletException;
import lombok.Getter;

@Getter
public class InvalidRequestPathParameterException extends ApplicationServletException {
    private Throwable cause;


    /**
     * <h3>Exception que extended a principal e lança os erros necessários</h3>
     *
     * @param message    -> <strong>Message de erro</strong>
     * @param httpStatus -> <strong>Código de erro</strong>
     */
    public InvalidRequestPathParameterException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }


    /**
     * <h3>Exception que extended a principal e lança os erros necessários</h3>
     *
     * @param message    -> <strong>Message de erro</strong>
     * @param httpStatus -> <strong>Código de erro</strong>
     * @param cause      -> <strong>Causa o erro no parâmetro da requisição</strong>
     */
    public InvalidRequestPathParameterException(String message, Integer httpStatus, Throwable cause) {
        super(message, httpStatus);
        this.cause = cause;
    }
}
