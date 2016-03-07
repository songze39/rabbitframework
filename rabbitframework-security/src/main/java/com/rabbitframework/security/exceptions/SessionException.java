package com.rabbitframework.security.exceptions;

/**
 * session异常类
 *
 * @author justin.liang
 */
public class SessionException extends SecurityException {
    public SessionException() {
        super();
    }

    public SessionException(String message) {
        super(message);
    }

    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionException(Throwable cause) {
        super(cause);
    }
}
