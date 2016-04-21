package com.rabbitframework.security;

public class ConfigurationException extends SecurityException {
    public ConfigurationException() {
        super();
    }


    public ConfigurationException(String message) {
        super(message);
    }


    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
