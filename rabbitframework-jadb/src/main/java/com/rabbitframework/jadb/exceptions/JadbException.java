package com.rabbitframework.jadb.exceptions;

public class JadbException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public JadbException() {
		super();
	}

	public JadbException(String message) {
		super(message);
	}

	public JadbException(String message, Throwable cause) {
		super(message, cause);
	}

	public JadbException(Throwable cause) {
		super(cause);
	}
}
