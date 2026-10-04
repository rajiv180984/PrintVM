package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;

@Document(collection = "TransactionDetails")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class TransactionDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4842338154516077071L;

	private String transId;
	private String error_code;
	private double tax;
	private String channelName;
	private String status;
	private String amount;
	private String refundStatus;
	/*card , -netbanking, -wallet- emi */	
	private String method;
	private String error_description;
	private JsonNode notes;
	private long createdAt;
	
	

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	
	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public JsonNode getNotes() {
		return notes;
	}

	public void setNotes(JsonNode notes) {
		this.notes = notes;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "TransactionDetails [transId=" + transId + "]";
	}
	
	
	
}
