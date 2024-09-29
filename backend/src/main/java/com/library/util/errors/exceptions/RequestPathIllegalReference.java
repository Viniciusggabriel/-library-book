package com.library.util.errors.exceptions;

import com.library.util.errors.throwables.ApplicationServletException;
import lombok.Getter;

@Getter
public class RequestPathIllegalReference extends ApplicationServletException {
    private Throwable cause;

    public RequestPathIllegalReference(String message, Integer httpStatus) {
        super(message, httpStatus);
    }

    public RequestPathIllegalReference(String message, Integer httpStatus, Throwable cause) {
        super(message, httpStatus);
        this.cause = cause;
    }
}
