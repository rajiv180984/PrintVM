package com.first.test.exception;

public class PaymentFailureException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2101583975309757931L;
	private String message;
	private int errorCode;

	public PaymentFailureException(String message, int errorCode) {
		super();
		this.message = message;
		this.errorCode = errorCode;
	}

	public PaymentFailureException(String message) {
		super();
		this.message = message;
		
	}

	public PaymentFailureException(Throwable cause) {
		super(cause);
		this.message = cause.getMessage();
	}

	public PaymentFailureException(String message, Throwable cause) {
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
