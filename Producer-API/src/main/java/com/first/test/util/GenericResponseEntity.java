package com.first.test.util;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

public class GenericResponseEntity<T> implements Serializable {

	private static final long serialVersionUID = -4193272109068974318L;

	T data;
	private String status;
	private int responseCode;

	
	public GenericResponseEntity(T data, String status, int responseCode) {
		super();
		this.data = data;
		this.status = status;
		this.responseCode = responseCode;
	}

	public GenericResponseEntity(T data, int responseCode) {
		super();
		this.data = data;
		this.status = ServiceStatus.SUCCESS.toString();
		this.responseCode = responseCode;
	}
	
	public GenericResponseEntity(T data) {
		super();
		this.data = data;
		this.status = ServiceStatus.SUCCESS.toString();
		this.responseCode = HttpStatus.OK.value();
	}
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

}
