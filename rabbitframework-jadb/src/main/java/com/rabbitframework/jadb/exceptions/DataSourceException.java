package com.rabbitframework.jadb.exceptions;

public class DataSourceException extends JadbException {
	private static final long serialVersionUID = 1L;

	public DataSourceException() {
		super();
	}

	public DataSourceException(String message) {
		super(message);
	}

	public DataSourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataSourceException(Throwable cause) {
		super(cause);
	}

}
