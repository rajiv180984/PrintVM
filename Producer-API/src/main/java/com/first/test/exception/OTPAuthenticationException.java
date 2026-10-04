package com.first.test.exception;

public class OTPAuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 284726656392880446L;
	private String message;

	public OTPAuthenticationException(String message) {
		super();
		this.message = message;
	}

	public OTPAuthenticationException(Throwable cause) {
		super(cause);
		this.message = cause.getMessage();
	}

	public OTPAuthenticationException(String message, Throwable cause) {
		super(cause);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
