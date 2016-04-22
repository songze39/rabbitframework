package com.rabbitframework.security.authc;

public class UnknownAccountException extends AccountException {


    public UnknownAccountException() {
        super();
    }


    public UnknownAccountException(String message) {
        super(message);
    }

    public UnknownAccountException(Throwable cause) {
        super(cause);
    }
    
    public UnknownAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
