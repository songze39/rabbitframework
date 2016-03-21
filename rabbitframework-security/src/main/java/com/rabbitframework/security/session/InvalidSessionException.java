package com.rabbitframework.security.session;

import com.rabbitframework.security.exceptions.SessionException;

public class InvalidSessionException extends SessionException {
    public InvalidSessionException() {
    }

    public InvalidSessionException(String message) {
        super(message);
    }

    public InvalidSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidSessionException(Throwable cause) {
        super(cause);
    }
}
