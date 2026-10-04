package com.first.test.exception;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.first.test.model.Domain;

@JsonAutoDetect
public class ApiErrorResponse extends Domain implements Serializable {

	private static final long serialVersionUID = 3368439385896102154L;
	// private HttpStatus status;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDateTime timestamp;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String error;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private String code;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private int status;

	private ApiErrorResponse() {
		timestamp = LocalDateTime.now();
	}

	ApiErrorResponse(Throwable ex) {
		this();
		// this.status = status;
		this.error = "Unexpected error";
		
	}
	
	ApiErrorResponse(String message, String errorCode) {
		this();

		this.error = message;
		this.code = errorCode;
	}
	
	ApiErrorResponse(String message, String errorCode, Throwable ex) {
		this();
		this.code = errorCode;
		this.error = message;
		
	}

	ApiErrorResponse(HttpStatus status, String message, Throwable ex) {
		this();
		this.error = message;
		
	}

	public ApiErrorResponse(String message, String errorCode, int statusCode) {
		this();
		this.code = errorCode;
		this.error = message;
		this.status = statusCode;
	}

	public ApiErrorResponse(String message, int errorCodeNo, int statusCode) {
		this();
		this.code = Integer.toString(errorCodeNo);
		this.error = message;
		this.status = statusCode;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	
}