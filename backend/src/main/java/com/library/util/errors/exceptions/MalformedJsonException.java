package com.library.util.errors.exceptions;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;

@Getter
public class MalformedJsonException extends JsonProcessingException {
    private final int statusCode;


    public MalformedJsonException(String message, Integer httpStatus) {
        super(message);
        this.statusCode = httpStatus;
    }

    public MalformedJsonException(String message, JsonLocation loc, Integer httpStatus) {
        super(message);
        this.statusCode = httpStatus;
    }
}
