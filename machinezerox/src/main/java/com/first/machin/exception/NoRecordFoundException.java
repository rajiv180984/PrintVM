package com.first.machin.exception;

public class NoRecordFoundException extends RuntimeException {

	private static final long serialVersionUID = 2847266557392880446L;
	private String message;
    private String errorCode;
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

	public NoRecordFoundException(String message, String errorCode) {
		super();
		this.message = message;
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	
}
