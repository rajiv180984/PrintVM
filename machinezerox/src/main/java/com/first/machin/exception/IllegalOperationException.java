package com.first.machin.exception;

public class IllegalOperationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2101583975309757931L;
	private String message;

	public IllegalOperationException(String message) {
		super();
		this.message = message;
	}

	public IllegalOperationException(Throwable cause) {
		super(cause);
		this.message = cause.getMessage();
	}

	public IllegalOperationException(String message, Throwable cause) {
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
