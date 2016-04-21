package com.rabbitframework.security;

public class UnavailableSecurityManagerException extends SecurityException {

    public UnavailableSecurityManagerException() {
        super();
    }

    public UnavailableSecurityManagerException(String message) {
        super(message);
    }

    public UnavailableSecurityManagerException(Throwable cause) {
        super(cause);
    }

    public UnavailableSecurityManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
