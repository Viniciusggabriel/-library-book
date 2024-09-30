package com.library.util.errors.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@Getter
@AllArgsConstructor
public class InputOutputDataException extends IOException {
    private final Integer httpStatus;

    /**
     * <h3>Exception que serve para personalizar erros de IO</h3>
     *
     * @param message    -> <strong>Message de erro</strong>
     * @param httpStatus -> <strong>Código de erro</strong>
     */
    public InputOutputDataException(String message, Integer httpStatus) {
        super(String.format("message: %s, status: %d", message, httpStatus));
        this.httpStatus = httpStatus;
    }
}
