package com.first.test.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="ReferralFriend")
public class ReferralFriend implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -349838618644786784L;
	@Indexed(unique=true)
	private String referalByEmailId;
	private String mobile;
	private String toWhomreferEmailId;
	private String referalCode;
	private LocalDateTime createOn;
	private LocalDateTime expireOn;
	private int count;
	private int earnedMoney;
	private int referralMoney;
	private String promotype;
	private boolean enable;
	
	
	public String getReferalByEmailId() {
		return referalByEmailId;
	}
	public void setReferalByEmailId(String referalByEmailId) {
		this.referalByEmailId = referalByEmailId;
	}
	public String getReferalEmailId() {
		return toWhomreferEmailId;
	}
	public void setReferalEmailId(String referalEmailId) {
		this.toWhomreferEmailId = referalEmailId;
	}
	public String getReferalCode() {
		return referalCode;
	}
	public void setReferalCode(String referalCode) {
		this.referalCode = referalCode;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getEarnedMoney() {
		return earnedMoney;
	}
	public void setEarnedMoney(int earnedMoney) {
		this.earnedMoney = earnedMoney;
	}
	public String getPromotype() {
		return promotype;
	}
	public void setPromotype(String promotype) {
		this.promotype = promotype;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	public LocalDateTime getCreateOn() {
		return createOn;
	}
	public void setCreateOn(LocalDateTime createOn) {
		this.createOn = createOn;
	}
	public LocalDateTime getExpireOn() {
		return expireOn;
	}
	public void setExpireOn(LocalDateTime expireOn) {
		this.expireOn = expireOn;
	}
	public int getReferralMoney() {
		return referralMoney;
	}
	public void setReferralMoney(int referralMoney) {
		this.referralMoney = referralMoney;
	}
	
	
	
}
