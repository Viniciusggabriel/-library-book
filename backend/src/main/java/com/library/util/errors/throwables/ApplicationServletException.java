package com.library.util.errors.throwables;

import jakarta.servlet.ServletException;
import lombok.Getter;

@Getter
public class ApplicationServletException extends ServletException {
    private final int statusCode;

    public ApplicationServletException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}