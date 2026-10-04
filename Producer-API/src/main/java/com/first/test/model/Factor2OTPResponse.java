package com.first.test.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

//{"Status":"Success","Details":"OTP Matched"}
@JsonAutoDetect
public class Factor2OTPResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3266601015701868523L;
	private String Status;
	private String Details;
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getDetails() {
		return Details;
	}
	public void setDetails(String details) {
		Details = details;
	}
	@Override
	public String toString() {
		return "Factor2OTPResponse [Status=" + Status + ", Details=" + Details + "]";
	}

	
}
