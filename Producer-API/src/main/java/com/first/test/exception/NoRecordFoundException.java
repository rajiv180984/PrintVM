package com.first.test.exception;

public class NoRecordFoundException extends RuntimeException {

	private static final long serialVersionUID = 2847266557392880446L;
	private String message;
    private int errorCode;
	public NoRecordFoundException(String message) {
		super();
		this.message = message;
	}

	public NoRecordFoundException(Throwable cause) {
		super(cause);
		this.message = cause.getMessage();
	}

	public NoRecordFoundException(String message, Throwable cause) {
		super(cause);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NoRecordFoundException(String message, int errorCode) {
		super();
		this.message = message;
		this.errorCode = errorCode;
	}

}
