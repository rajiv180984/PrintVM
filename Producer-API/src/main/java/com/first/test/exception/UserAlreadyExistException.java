package com.first.test.exception;

public class UserAlreadyExistException extends RuntimeException {

	private static final long serialVersionUID = 2847266557392880446L;
	private String message;
	private int errorCode;
	

	public UserAlreadyExistException(String message) {
		super();
		this.message = message;
	}

	public UserAlreadyExistException(Throwable cause) {
		super(cause);
		this.message = cause.getMessage();
	}

	public UserAlreadyExistException(String message, Throwable cause) {
		super(cause);
		this.message = message;
	}
	
	

	public UserAlreadyExistException(String message, int errorCode) {
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
