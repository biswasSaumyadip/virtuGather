package com.event.virtugather.exceptions;

import java.io.Serial;

public class InvalidArgumentException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidArgumentException() {
        super();
    }

    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidArgumentException(Throwable cause) {
        super(cause);
    }
}