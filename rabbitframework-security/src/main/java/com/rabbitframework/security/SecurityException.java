package com.rabbitframework.security;

/**
 * 框架异常处理超类,继承{@link RuntimeException}
 *
 * @author: justin.liang
 * @date: 16/4/18 下午10:33
 */
public class SecurityException extends RuntimeException {
    public SecurityException() {
        super();
    }

    public SecurityException(String message) {
        super(message);
    }

    public SecurityException(Throwable cause) {
        super(cause);
    }

    public SecurityException(String message, Throwable cause) {
        super(message, cause);
    }
}
