package com.library.util.errors.exceptions;

public class UserInLibraryNotFound extends RuntimeException {
    public UserInLibraryNotFound(String message) {
        super(message);
    }

    public UserInLibraryNotFound(String message, Integer httpStatus) {
        super(String.format("message: %s, status: %d", message, httpStatus));
    }
}
