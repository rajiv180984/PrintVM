package com.first.test.model;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection="Customer")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class Customer implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2204688521810268354L;
	private String custId;
	private String wid;
	private float balance;
	private String custName;
	@Indexed(unique=true)
	private String emailId;
	@Indexed(unique=true)
	private String mobileNo;
	private String Address;
	private String cityName;
	private String stateName;
	private String pinCode;
	private UserCredentials userCredentials;
	private String activeStatus;
	private String profession;
	private String education;
	private String role;
	@NotEmpty
	private String password;
	// This unnique used to refer and earn to friend
	private String referralCode;
	// this unique code is used during signup and this code is referal code that was given by his refferal or Friend 
	private String promocode;
	
	public String getId() {
		return custId;
	}
	public void setId(String id) {
		this.custId = id;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	
	
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public UserCredentials getUserCredentials() {
		return userCredentials;
	}
	public void setUserCredentials(UserCredentials userCredentials) {
		this.userCredentials = userCredentials;
	}
	public String getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getReferralCode() {
		return referralCode;
	}
	public void setReferralCode(String referralCode) {
		this.referralCode = referralCode;
	}
	
	public String getPromocode() {
		return promocode;
	}
	public void setPromocode(String promocode) {
		this.promocode = promocode;
	}
	
	
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "Customer [id=" + custId + ", custName=" + custName + ", emailId=" + emailId + ", mobileNo=" + mobileNo
				+ ", Address=" + Address + ", cityName=" + cityName + ", stateName=" + stateName + ", pinCode="
				+ pinCode + "]";
	}
	
	
}


