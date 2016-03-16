package com.rabbitframework.security.exceptions;

public class UnsupportedTokenException extends AuthenticationException {

    /**
     * Creates a new UnsupportedTokenException.
     */
    public UnsupportedTokenException() {
        super();
    }

    /**
     * Constructs a new UnsupportedTokenException.
     *
     * @param message the reason for the exception
     */
    public UnsupportedTokenException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnsupportedTokenException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public UnsupportedTokenException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new UnsupportedTokenException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public UnsupportedTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
