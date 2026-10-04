package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection="WalletDetails")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class WalletModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6846410268775617300L;
	@Indexed(unique=true)
	private String wid;
	private String custId;
	@Indexed(unique=true)
	private String emailId;
	private String mobileNo;
	// to Protect the Wallet
	private String securityKey;
	// Yet to decide
	private String privateKey;
	private long createDate;
	private long lastTransactiotime;
	private float balance;
	// amount lock till print request job is conclude completely
	private float lockamount;
	private String currencyName;
	private boolean enable;
	private int redeemPoint;
	
	public String getWid() {
		return wid;
	}
	public void setWid(String wid) {
		this.wid = wid;
	}
	public String getCustId() {
		return custId;
	}
	
	
	public float getLockamount() {
		return lockamount;
	}
	public void setLockamount(float lockamount) {
		this.lockamount = lockamount;
	}
	public void setCustId(String custId) {
		this.custId = custId;
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
	public String getSecurityKey() {
		return securityKey;
	}
	public void setSecurityKey(String securityKey) {
		this.securityKey = securityKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	public long getLastTransactiotime() {
		return lastTransactiotime;
	}
	public void setLastTransactiotime(long lastTransactiotime) {
		this.lastTransactiotime = lastTransactiotime;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	public int getRedeemPoint() {
		return redeemPoint;
	}
	public void setRedeemPoint(int redeemPoint) {
		this.redeemPoint = redeemPoint;
	}
	@Override
	public String toString() {
		return "WalletModel [wid=" + wid + ", custId=" + custId + ", emailId=" + emailId + ", mobileNo=" + mobileNo
				+ ", securityKey=" + securityKey + ", privateKey=" + privateKey + ", createDate=" + createDate
				+ ", lastTransactiotime=" + lastTransactiotime + ", balance=" + balance + ", currencyName="
				+ currencyName + ", enable=" + enable + ", redeemPoint=" + redeemPoint + "]";
	}

	
}
