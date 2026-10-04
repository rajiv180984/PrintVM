package com.first.test.exception;

public class InsufficientParamException extends RuntimeException {
	private static final long serialVersionUID = -8460356990632230194L;

	private ErrorCode code;
	private String message;

	public InsufficientParamException(String message) {
		super(message);
		this.message = message;
	}

	public InsufficientParamException(String message, Throwable cause) {
		super(cause);
		this.message = message;
	}
	
	public InsufficientParamException(String message, Throwable cause, ErrorCode code) {
		super(message, cause);
		this.code = code;
	}

}
