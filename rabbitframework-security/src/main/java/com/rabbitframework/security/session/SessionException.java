package com.rabbitframework.security.session;

import com.rabbitframework.security.SecurityException;

/**
 * @author: justin.liang
 * @date: 16/4/18 下午11:32
 */
public class SessionException extends SecurityException {
    public SessionException() {
        super();
    }

    public SessionException(String message) {
        super(message);
    }

    public SessionException(Throwable cause) {
        super(cause);
    }

    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
