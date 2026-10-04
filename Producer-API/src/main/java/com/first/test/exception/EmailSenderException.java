package com.first.test.exception;

public class EmailSenderException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -649232080925404849L;
	public EmailSenderException(String msg){
		super(msg);
	}

}
