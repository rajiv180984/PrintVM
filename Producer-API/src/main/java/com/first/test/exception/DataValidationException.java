package com.first.test.exception;

public class DataValidationException extends RuntimeException {

	private static final long serialVersionUID = 2847266557392880446L;
	private String message;
    private int errorCode;
    
	public DataValidationException(String message) {
		super();
		this.message = message;
	}

	public DataValidationException(String message, int errorCode) {
		super();
		this.message = message;
		this.errorCode = errorCode;
	}
	public DataValidationException(Throwable cause) {
		super(cause);
		this.message = cause.getMessage();
	}

	public DataValidationException(String message, Throwable cause) {
		super(cause);
		this.message = message;
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
