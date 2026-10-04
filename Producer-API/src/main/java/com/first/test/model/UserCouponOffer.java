package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="UserCouponOffer")
public class UserCouponOffer implements Serializable{

	private static final long serialVersionUID = -8460311445799750451L;
	private String custId;
	private String mobileNo;
	private String  couponscode;
	private boolean  used;
	private long expireon;
	private float offerValue;
	
	public String getCouponscode() {
		return couponscode;
	}
	public void setCouponscode(String couponscode) {
		this.couponscode = couponscode;
	}
	public boolean getUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	public long getExpireon() {
		return expireon;
	}
	public void setExpireon(long expireon) {
		this.expireon = expireon;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public float getOfferValue() {
		return offerValue;
	}
	public void setOfferValue(float offerValue) {
		this.offerValue = offerValue;
	}
	
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	@Override
	public String toString() {
		return "UserCouponOffer [custId=" + custId + ", couponscode=" + couponscode + ", Used=" + used + ", expireon="
				+ expireon + ", offerValue=" + offerValue + "]";
	} 
	
	
}
