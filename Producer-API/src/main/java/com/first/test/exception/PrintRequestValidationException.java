package com.first.test.exception;

public class PrintRequestValidationException extends RuntimeException {

	private static final long serialVersionUID = 2847266557392880446L;
	private String message;
    private int errorCode;
    
	public PrintRequestValidationException(String message) {
		super();
		this.message = message;
	}

	public PrintRequestValidationException(String message, int errorCode) {
		super();
		this.message = message;
		this.errorCode = errorCode;
	}
	public PrintRequestValidationException(Throwable cause) {
		super(cause);
		this.message = cause.getMessage();
	}

	public PrintRequestValidationException(String message, Throwable cause) {
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
