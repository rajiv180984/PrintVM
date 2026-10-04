package com.first.test.model;

import java.io.Serializable;

public class PartnerItemRequest  extends Domain  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7998184093582415039L;
	private String partnerId;
	private String docId;
	private String machineId;
	private String mobileNo;
	private String securityCode;
	private String paymentStr; // payment amount, payment gateway details
	private String paymentStatus;
	private double balanceDue;
	private String requestType;
	
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public String getPaymentStr() {
		return paymentStr;
	}
	public void setPaymentStr(String paymentStr) {
		this.paymentStr = paymentStr;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public double getBalanceDue() {
		return balanceDue;
	}
	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	
}

