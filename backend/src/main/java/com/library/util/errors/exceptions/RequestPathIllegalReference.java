package com.library.util.errors.exceptions;

import jakarta.servlet.ServletException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RequestPathIllegalReference extends ServletException {
    private final Integer httpStatus;

    public RequestPathIllegalReference(String message, Integer httpStatus) {
        super(String.format("message: %s, status: %d", message, httpStatus));
        this.httpStatus = httpStatus;
    }
}
