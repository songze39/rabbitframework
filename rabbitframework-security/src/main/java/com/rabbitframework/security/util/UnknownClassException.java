package com.rabbitframework.security.util;

import com.rabbitframework.security.SecurityException;

/**
 * @author: justin.liang
 * @date: 16/4/18 下午10:44
 */
public class UnknownClassException extends SecurityException {
    public UnknownClassException() {
        super();
    }

    public UnknownClassException(String message) {
        super(message);
    }

    public UnknownClassException(Throwable cause) {
        super(cause);
    }

    public UnknownClassException(String message, Throwable cause) {
        super(message, cause);
    }
}
