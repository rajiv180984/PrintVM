package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection="ZeroxPaymentDetails")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class ZeroxPaymentDetails implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8860182147360724765L;
	// Payment Details
	@Indexed(unique=true)
    private String walletId;
	@Indexed(unique=true)
	private String custId;
	private String userId;
	private String mobileNo;
	@Indexed(unique=true)
	private String emailId;
	private float zeroxBalance;
	private ThirdPartyPaymentDetails thirdPartyPaymentDetails;
	
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public float getZeroxBalance() {
		return zeroxBalance;
	}
	public void setZeroxBalance(float zeroxBalance) {
		this.zeroxBalance = zeroxBalance;
	}
	public ThirdPartyPaymentDetails getThirdPartyPaymentDetails() {
		return thirdPartyPaymentDetails;
	}
	public void setThirdPartyPaymentDetails(ThirdPartyPaymentDetails thirdPartyPaymentDetails) {
		this.thirdPartyPaymentDetails = thirdPartyPaymentDetails;
	}
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
	@Override
	public String toString() {
		return "ZeroxPaymentDetails [custId=" + custId + ", userId=" + userId + ", mobileNo=" + mobileNo + ", emailId="
				+ emailId + ", zeroxBalance=" + zeroxBalance + ", thirdPartyPaymentDetails=" + thirdPartyPaymentDetails
				+ "]";
	}
	
	
	
}
