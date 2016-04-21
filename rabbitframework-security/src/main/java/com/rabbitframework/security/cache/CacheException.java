package com.rabbitframework.security.cache;

import com.rabbitframework.security.SecurityException;

/**
 * @author: justin.liang
 * @date: 16/4/21 下午5:19
 */
public class CacheException extends SecurityException {
    public CacheException() {
        super();
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
}
