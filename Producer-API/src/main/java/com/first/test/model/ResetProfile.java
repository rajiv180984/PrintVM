package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="ResetProfile")
public class ResetProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 936297327498055399L;

	private String email;
	private String phone;
	private String custId;
	private String OTP;
	private String tokenStr;
	private String resetURL;
	private String userName;
	private boolean isUsed;
	private String newPassword;
	private long createdOn;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getOTP() {
		return OTP;
	}
	public void setOTP(String oTP) {
		OTP = oTP;
	}
	public String getTokenStr() {
		return tokenStr;
	}
	public void setTokenStr(String tokenStr) {
		this.tokenStr = tokenStr;
	}
	public String getResetURL() {
		return resetURL;
	}
	public void setResetURL(String resetURL) {
		this.resetURL = resetURL;
	}
	public boolean isUsed() {
		return isUsed;
	}
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	public long getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(long createdOn) {
		this.createdOn = createdOn;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
	
}
