package com.first.test.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.sun.istack.NotNull;

@Document(collection = "OrderDetails")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class OrderDetails implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 214181969965318236L;
	@Indexed(unique=true)
	@NotEmpty
	@NotNull
	private String orderId;
	private String amount;
	private String currency;
	private String receipt;
	private String status;
	private String payment_capture;
	private Timestamp createdAt;
	private int attempts;
	private Map<String, String> notes;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getReceipt() {
		return receipt;
	}
	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}
	public String getPayment_capture() {
		return payment_capture;
	}
	public void setPayment_capture(String payment_capture) {
		this.payment_capture = payment_capture;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Map<String, String> getNotes() {
		return notes;
	}
	public void setNotes(Map<String, String> notes) {
		this.notes = notes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getAttempts() {
		return attempts;
	}
	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	
}
