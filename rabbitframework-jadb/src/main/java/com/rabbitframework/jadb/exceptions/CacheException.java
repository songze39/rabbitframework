package com.rabbitframework.jadb.exceptions;

public class CacheException extends JadbException {
	private static final long serialVersionUID = 1L;

	public CacheException() {
		super();
	}

	public CacheException(String msg) {
		super(msg);
	}

	public CacheException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public CacheException(Throwable throwable) {
		super(throwable);
	}
}
