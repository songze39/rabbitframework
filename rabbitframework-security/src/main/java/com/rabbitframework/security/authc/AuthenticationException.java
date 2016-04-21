package com.rabbitframework.security.authc;

import com.rabbitframework.security.SecurityException;

/**
 * @author: justin.liang
 * @date: 16/4/19 下午11:15
 */
public class AuthenticationException extends SecurityException {
    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
