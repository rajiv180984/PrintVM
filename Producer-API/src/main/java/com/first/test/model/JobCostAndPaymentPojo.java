package com.first.test.model;

import java.io.Serializable;

public class JobCostAndPaymentPojo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6392692939440986648L;
	private String custId;
	private String mobileNo;
	private String  couponCode;
	private boolean isVerifyCoupon;
	private int couponAmount;
	private float costDerivative;
	private int numberOfPage;
	private String balanceFromZeroxWallet;
	private String isGuestUser;
	private String isZeroxUser;
	
	private float taxAmount;
	private float totalPaidAmount;
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public boolean isVerifyCoupon() {
		return isVerifyCoupon;
	}
	public void setVerifyCoupon(boolean isVerifyCoupon) {
		this.isVerifyCoupon = isVerifyCoupon;
	}
	public int getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(int couponAmount) {
		this.couponAmount = couponAmount;
	}
	public float getCostDerivative() {
		return costDerivative;
	}
	public void setCostDerivative(float costDerivative) {
		this.costDerivative = costDerivative;
	}
	public int getNumberOfPage() {
		return numberOfPage;
	}
	public void setNumberOfPage(int numberOfPage) {
		this.numberOfPage = numberOfPage;
	}
	public String getBalanceFromZeroxWallet() {
		return balanceFromZeroxWallet;
	}
	public void setBalanceFromZeroxWallet(String balanceFromZeroxWallet) {
		this.balanceFromZeroxWallet = balanceFromZeroxWallet;
	}
	public String getIsGuestUser() {
		return isGuestUser;
	}
	public void setIsGuestUser(String isGuestUser) {
		this.isGuestUser = isGuestUser;
	}
	public String getIsZeroxUser() {
		return isZeroxUser;
	}
	public void setIsZeroxUser(String isZeroxUser) {
		this.isZeroxUser = isZeroxUser;
	}
	public float getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(float taxAmount) {
		this.taxAmount = taxAmount;
	}
	public float getTotalPaidAmount() {
		return totalPaidAmount;
	}
	public void setTotalPaidAmount(float totalPaidAmount) {
		this.totalPaidAmount = totalPaidAmount;
	}
	
	
	
}
