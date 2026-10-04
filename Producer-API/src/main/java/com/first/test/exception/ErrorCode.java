package com.first.test.exception;

import java.io.Serializable;

public class ErrorCode implements Serializable {

	private static final long serialVersionUID = 2372398211950933057L;

	private String message;
	private String errorCode;

	public ErrorCode(String message, String errorCode) {
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

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "ErrorCode [message=" + message + ", errorCode=" + errorCode + "]";
	}

}
