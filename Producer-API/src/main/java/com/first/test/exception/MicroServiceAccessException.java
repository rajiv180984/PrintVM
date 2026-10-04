package com.first.test.exception;

public class MicroServiceAccessException extends RuntimeException {

	private static final long serialVersionUID = -8460356990632230194L;
	

	public MicroServiceAccessException() {
		super();
		
	}

	public MicroServiceAccessException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public MicroServiceAccessException(String message) {
		super(message);
		
	}
}
