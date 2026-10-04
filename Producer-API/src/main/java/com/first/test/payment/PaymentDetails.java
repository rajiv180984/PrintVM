package com.first.test.payment;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;

@Document(collection="PaymentDetails")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class PaymentDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 809791558451201365L;
	
	@NotNull
	private String orderId;
	@NotNull
	private String custId;
	@NotNull
	private String mobileNo;
	@NotNull
	private String emailId;
	@NotNull
	private String txnAmount;
	@NotNull
	private String printJobId;
	private String paymentGatewayName;
	private long paymentTimestamp;
	private String paymentStatus;
	private JsonNode refundDetails;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(String txnAmount) {
		this.txnAmount = txnAmount;
	}

	public String getPrintJobId() {
		return printJobId;
	}

	public void setPrintJobId(String printJobId) {
		this.printJobId = printJobId;
	}

	public String getPaymentGatewayName() {
		return paymentGatewayName;
	}

	public void setPaymentGatewayName(String paymentGatewayName) {
		this.paymentGatewayName = paymentGatewayName;
	}

	public long getPaymentTimestamp() {
		return paymentTimestamp;
	}

	public void setPaymentTimestamp(long paymentTimestamp) {
		this.paymentTimestamp = paymentTimestamp;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public JsonNode getRefundDetails() {
		return refundDetails;
	}

	public void setRefundDetails(JsonNode refundDetails) {
		this.refundDetails = refundDetails;
	}

}

/*
 * { "orderId": "eyu134", "custId": "user1", "mobileNo": "8904668406",
 * "emailId": "rathor.rajeev@gmail.com", "txnAmount": "20", "jobData": {
 * "fileName": "sdfjk", "officeName": "Real-State", "count": 10 } }
 * 
 */
