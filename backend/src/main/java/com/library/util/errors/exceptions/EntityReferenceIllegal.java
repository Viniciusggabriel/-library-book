package com.library.util.errors.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EntityReferenceIllegal extends RuntimeException {
    private final Integer httpStatus;

    public EntityReferenceIllegal(String message, Integer httpStatus) {
        super(String.format("message: %s, status: %d", message, httpStatus));
        this.httpStatus = httpStatus;
    }
}
