package com.library.util.errors.exceptions;

import jakarta.servlet.ServletException;

public class RequestPathIllegalReference extends ServletException {
    public RequestPathIllegalReference(String message) {
        super(message);
    }

    public RequestPathIllegalReference(String message, Integer httpStatus) {
        super(String.format("message: %s, status: %d", message, httpStatus));

    }
}
