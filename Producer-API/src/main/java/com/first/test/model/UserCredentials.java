package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="UserCredentials")
public class UserCredentials implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1836936301246676337L;
	private String uuid;
	@Indexed(unique=true)
	private String userName;
	private String password;
	// Generate by system 
	private String securityKey;
	
	private long expireOn;
	//Generate and send
	private String OTP;
	
	//generate and send
	private String webLinkForActivation;
	
	private String status;
	

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecurityKey() {
		return securityKey;
	}

	public void setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
	}

	public String getOTP() {
		return OTP;
	}

	public void setOTP(String oTP) {
		OTP = oTP;
	}

	public String getWebLinkForActivation() {
		return webLinkForActivation;
	}

	public void setWebLinkForActivation(String webLinkForActivation) {
		this.webLinkForActivation = webLinkForActivation;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserCredentials [uuid=" + uuid + ", userName=" + userName + ", password=" + password + ", securityKey="
				+ securityKey + ", OTP=" + OTP + ", webLinkForActivation=" + webLinkForActivation + "]";
	}

	
}
