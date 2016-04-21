package com.rabbitframework.security.authz;

import com.rabbitframework.security.SecurityException;

/**
 * @author: justin.liang
 * @date: 16/4/19 下午10:26
 */
public class AuthorizationException extends SecurityException {
    public AuthorizationException() {
        super();
    }

    public AuthorizationException(String message) {
        super(message);
    }

    public AuthorizationException(Throwable cause) {
        super(cause);
    }

    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}
