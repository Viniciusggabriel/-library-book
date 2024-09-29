package com.library.util.errors.exceptions;

import com.library.util.errors.throwables.ApplicationRuntimeException;

public class ValueNotFound extends ApplicationRuntimeException {
    public ValueNotFound(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
