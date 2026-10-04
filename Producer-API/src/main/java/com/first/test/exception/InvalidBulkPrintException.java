package com.first.test.exception;

public class InvalidBulkPrintException extends RuntimeException {

	private static final long serialVersionUID = 2847266557392880446L;
	private String message;

	public InvalidBulkPrintException(String message) {
		super();
		this.message = message;
	}

	public InvalidBulkPrintException(Throwable cause) {
		super(cause);
		this.message = cause.getMessage();
	}

	public InvalidBulkPrintException(String message, Throwable cause) {
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
