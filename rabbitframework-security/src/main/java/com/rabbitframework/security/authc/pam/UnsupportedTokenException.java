package com.rabbitframework.security.authc.pam;

import com.rabbitframework.security.authc.AuthenticationException;

public class UnsupportedTokenException extends AuthenticationException {

    public UnsupportedTokenException() {
        super();
    }

    public UnsupportedTokenException(String message) {
        super(message);
    }

    public UnsupportedTokenException(Throwable cause) {
        super(cause);
    }

    public UnsupportedTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
