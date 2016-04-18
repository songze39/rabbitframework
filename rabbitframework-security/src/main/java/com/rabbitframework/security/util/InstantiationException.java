package com.rabbitframework.security.util;

import com.rabbitframework.security.SecurityException;

public class InstantiationException extends SecurityException {
    public InstantiationException() {
        super();
    }

    public InstantiationException(String message) {
        super(message);
    }

    public InstantiationException(Throwable cause) {
        super(cause);
    }

    public InstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
