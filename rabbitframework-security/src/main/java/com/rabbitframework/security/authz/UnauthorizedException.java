package com.rabbitframework.security.authz;

import com.rabbitframework.security.exceptions.AuthorizationException;

public class UnauthorizedException extends AuthorizationException {
    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }
}
