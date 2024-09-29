package com.library.util.errors.exceptions;

import com.library.util.errors.throwables.ApplicationRuntimeException;

public class PortHttpServerRuntimeException extends ApplicationRuntimeException {
    public PortHttpServerRuntimeException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
