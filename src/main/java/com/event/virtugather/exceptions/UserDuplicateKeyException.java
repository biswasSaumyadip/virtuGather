package com.event.virtugather.exceptions;

public class UserDuplicateKeyException extends RuntimeException {

    public UserDuplicateKeyException(String message) {
        super(message); // Pass a descriptive message
    }

    public UserDuplicateKeyException(String message, Throwable cause) {
        super(message, cause); // Include the original cause of the exception
    }
}
