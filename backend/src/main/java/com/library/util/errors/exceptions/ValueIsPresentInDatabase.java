package com.library.util.errors.exceptions;

import com.library.util.errors.throwables.ApplicationRuntimeException;

public class ValueIsPresentInDatabase extends ApplicationRuntimeException {
    public ValueIsPresentInDatabase(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
