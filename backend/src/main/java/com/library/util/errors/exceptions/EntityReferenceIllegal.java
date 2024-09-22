package com.library.util.errors.exceptions;

public class EntityReferenceIllegal extends RuntimeException {
    public EntityReferenceIllegal(String message) {
        super(message);
    }

    public EntityReferenceIllegal(String message, Integer httpStatus) {
        super(String.format("message: %s, status: %d", message, httpStatus));

    }
}
