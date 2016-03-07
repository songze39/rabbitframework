package com.rabbitframework.security.exceptions;

/**
 * 异常处理根类,继承{@link RuntimeException}
 * 所有异常类都需要继承此类
 *
 * @author justin.liang
 */
public class SecurityException extends RuntimeException {
    public SecurityException() {
        super();
    }

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }
}
