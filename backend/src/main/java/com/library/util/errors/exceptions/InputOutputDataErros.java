package com.library.util.errors.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@Getter
@AllArgsConstructor
public class InputOutputDataErros extends IOException {
    private final Integer httpStatus;

    public InputOutputDataErros(String message, Integer httpStatus) {
        super(String.format("message: %s, status: %d", message, httpStatus));
        this.httpStatus = httpStatus;
    }
}
