package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="ThirdPartyPaymentDetails")
public class ThirdPartyPaymentDetails implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -71548084522229614L;
	private String mobileNo;
	private String emailId;
	private String custId;
	private String paymentRequest;
	private String paymentResponse;
	
	
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getPaymentRequest() {
		return paymentRequest;
	}
	public void setPaymentRequest(String paymentRequest) {
		this.paymentRequest = paymentRequest;
	}
	public String getPaymentResponse() {
		return paymentResponse;
	}
	public void setPaymentResponse(String paymentResponse) {
		this.paymentResponse = paymentResponse;
	}
	
	
}

