package com.library.util.errors.exceptions;

import com.library.util.errors.throwables.ApplicationRuntimeException;
import org.eclipse.jetty.http.HttpStatus;

public class InternalServerRuntimeException extends ApplicationRuntimeException {
    public InternalServerRuntimeException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR_500);
    }
}
