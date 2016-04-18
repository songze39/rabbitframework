package com.rabbitframework.security.session;

/**
 * @author: justin.liang
 * @date: 16/4/18 下午11:34
 */
public class InvalidSessionException extends SessionException {
    public InvalidSessionException() {
        super();
    }

    public InvalidSessionException(String message) {
        super(message);
    }

    public InvalidSessionException(Throwable cause) {
        super(cause);
    }

    public InvalidSessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
