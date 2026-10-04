package com.first.test.exception;

public class ProfileException extends RuntimeException {

	private static final long serialVersionUID = 2847266557392880446L;
	private String message;
	private int errorCode;
	

	public ProfileException(String message) {
		super();
		this.message = message;
	}

	public ProfileException(Throwable cause) {
		super(cause);
		this.message = cause.getMessage();
	}

	public ProfileException(String message, Throwable cause) {
		super(cause);
		this.message = message;
	}
	
	

	public ProfileException(String message, int errorCode) {
		super();
		this.message = message;
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
