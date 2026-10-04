package com.first.test.model;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="OTPRecord")
public class OTPRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3170352582467517311L;
	
	@Indexed(unique=true, expireAfterSeconds=18000)
	private String mobile;
	private String OTP;
	private Timestamp createOn;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOTP() {
		return OTP;
	}
	public void setOTP(String oTP) {
		OTP = oTP;
	}
	public Timestamp getCreateOn() {
		return createOn;
	}
	public void setCreateOn(Timestamp createOn) {
		this.createOn = createOn;
	}
	
	

}
