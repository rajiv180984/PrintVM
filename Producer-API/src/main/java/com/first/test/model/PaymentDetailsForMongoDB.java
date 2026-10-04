package com.first.test.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection="PaymentDetails")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class PaymentDetailsForMongoDB  implements Serializable{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -8979827284257725809L;
	private static DateFormat f = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
	
	@NotNull
	@Indexed(unique=true)
	private String transId;
	
	@Indexed(unique=true)
	@NotNull
	private String orderId;
	private String custId;
	private String custName;
	
	@NotNull
	@NotEmpty
	@Indexed
	private String emailId;
	private String mobileNo;
	
	@NotNull
	@NotEmpty
	private String wid;
	private float amount;
	
	private String transType;
	
	@NotNull
	@NotEmpty
	private String transStatus;
	//private OrderDetails orderData;
	private TransactionDetails transactionData;
	private long createDate;
	
	private String transDate;
	private String imei;
	private String receipt;
	private int attempts;
	
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

	public String getWid() {
		return wid;
	}

	public void setWid(String wid) {
		this.wid = wid;
	}

	public String getTransDate() {
		Date date=new Date(this.createDate);  
		this.transDate = f.format(date);
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
		
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	/*public OrderDetails getOrderData() {
		return orderData;
	}

	public void setOrderData(OrderDetails orderData) {
		this.orderData = orderData;
	}*/

	public TransactionDetails getTransactionData() {
		return transactionData;
	}

	public void setTransactionData(TransactionDetails transactionData) {
		this.transactionData = transactionData;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	@Override
	public String toString() {
		return "PaymentDetailsForMongoDB [transId=" + transId + ", orderId=" + orderId + ", custId=" + custId
				+ ", custName=" + custName + ", emailId=" + emailId + ", mobileNo=" + mobileNo + ", wid=" + wid
				+ ", amount=" + amount + ", transType=" + transType + ", transStatus=" + transStatus
				+ ", transactionData=" + transactionData + ", createDate=" + createDate + ", imei=" + imei
				+ ", receipt=" + receipt + ", attempts=" + attempts + "]";
	}

	
}
