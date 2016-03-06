package com.rabbitframework.jadb.exceptions;

public class BuilderException extends JadbException {
	private static final long serialVersionUID = 1L;

	public BuilderException() {
		super();
	}

	public BuilderException(String message) {
		super(message);
	}

	public BuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	public BuilderException(Throwable cause) {
		super(cause);
	}
}
