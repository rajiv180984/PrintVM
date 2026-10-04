package com.first.test.exception;

public class InsufficientPriviledgeException extends RuntimeException {

	private static final long serialVersionUID = 2847266557392880446L;
	private String message;

	public InsufficientPriviledgeException(String message) {
		super();
		this.message = message;
	}

	public InsufficientPriviledgeException(Throwable cause) {
		super(cause);
		this.message = cause.getMessage();
	}

	public InsufficientPriviledgeException(String message, Throwable cause) {
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
