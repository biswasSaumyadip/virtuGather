package com.event.virtugather.exceptions;

import java.io.Serial;

public class DatabaseAccessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public DatabaseAccessException() {
        super();
    }

    public DatabaseAccessException(String message) {
        super(message);
    }

    public DatabaseAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseAccessException(Throwable cause) {
        super(cause);
    }
}