package com.rabbitframework.security.exceptions;


public class UnavailableSecurityManagerException extends SecurityException {
    public UnavailableSecurityManagerException() {
        super();
    }

    public UnavailableSecurityManagerException(String message) {
        super(message);
    }

    public UnavailableSecurityManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnavailableSecurityManagerException(Throwable cause) {
        super(cause);
    }
}
