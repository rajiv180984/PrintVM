package com.first.machin.exception;

public class InvalidDataException extends RuntimeException {
	private String message;
	private int errorCode;
	
	public InvalidDataException(String message) {
		super(message);
		this.message = message;
		
	}
	public InvalidDataException(String message, int i) {
		super(message);
		this.message = message;
		this.errorCode = i;
	}

}
