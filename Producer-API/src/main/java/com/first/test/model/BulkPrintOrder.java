package com.first.test.model;

import java.io.Serializable;
import java.sql.Timestamp;
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
import com.first.test.model.Address;


/*
 * number Of Copy and Number of page
 * Type of paper
 * Type of Binding
 * OrderId
 * Side Print
 * 
 */

@Document(collection="BulkPrintOrder")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class BulkPrintOrder implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3320097180828030544L;
	private static DateFormat f = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
	
	@Indexed(unique=true)
	private String orderId;
	@NotNull
	private String emailId;
	private String mobileNo;
	//@NotEmpty
	private int noOfCopy;
	// NoofCopy * pageCount from one print set evaluate from expression
	private int totalPageCount;
	private String paperType;
	private PrintProperties printProperties;
	
	private Binding binding;
	private String s3filePath;
	//@NotNull
	private Address deliveryAddress;
	private String orderType;
	private BulkOrderCostEvaluation buldOrderCost;
	private String partnerId;
	private String orderStaus;
	private String paymentStatus;
	private boolean sufficientAmount;
	private String couponCode;
	private long createDate;
	private String createOn;
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public int getNoOfCopy() {
		return noOfCopy;
	}
	public void setNoOfCopy(int noOfCopy) {
		this.noOfCopy = noOfCopy;
	}
	public String getPaperType() {
		return paperType;
	}
	public void setPaperType(String paperType) {
		this.paperType = paperType;
	}
	public String getS3filePath() {
		return s3filePath;
	}
	public void setS3filePath(String s3filePath) {
		this.s3filePath = s3filePath;
	}
	public Address getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public PrintProperties getPrintProperties() {
		return printProperties;
	}
	public void setPrintProperties(PrintProperties printProperties) {
		this.printProperties = printProperties;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public Binding getBinding() {
		return binding;
	}
	public void setBinding(Binding binding) {
		this.binding = binding;
	}

	
	public BulkOrderCostEvaluation getBuldOrderCost() {
		return buldOrderCost;
	}
	public void setBuldOrderCost(BulkOrderCostEvaluation buldOrderCost) {
		this.buldOrderCost = buldOrderCost;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getOrderStaus() {
		return orderStaus;
	}
	public void setOrderStaus(String orderStaus) {
		this.orderStaus = orderStaus;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public boolean isSufficientAmount() {
		return sufficientAmount;
	}
	public void setSufficientAmount(boolean sufficientAmount) {
		this.sufficientAmount = sufficientAmount;
	}
	public long getCreateDate() {
		return createDate;
	}
	public void setCreateDate(long createDate) {
		this.createDate = System.currentTimeMillis();
	}
	public String getCreateOn() {
		Date date=new Date(this.createDate);  
		this.createOn = f.format(date);
	
		return createOn;
	}
	public void setCreateOn(String createOn) {
		Date date=new Date(this.createDate);  
	
		
		this.createOn = f.format(date);
	}
	public int getTotalPageCount() {
		return totalPageCount;
	}
	public void setTotalPageCount(int totalPageCount) {
		this.totalPageCount = totalPageCount;
	}
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	@Override
	public String toString() {
		return "BulkPrintOrder [orderId=" + orderId + ", emailId=" + emailId + ", mobileNo=" + mobileNo + ", noOfCopy="
				+ noOfCopy + ", totalPageCount=" + totalPageCount + ", paperType=" + paperType + ", printProperties="
				+ printProperties + ", binding=" + binding + ", s3filePath=" + s3filePath + ", deliveryAddress="
				+ deliveryAddress + ", orderType=" + orderType + ", buldOrderCost=" + buldOrderCost + ", partnerId="
				+ partnerId + ", orderStaus=" + orderStaus + ", paymentStatus=" + paymentStatus + ", sufficientAmount="
				+ sufficientAmount + ", couponCode=" + couponCode + ", createDate=" + createDate + ", createOn="
				+ createOn + "]";
	}
	
	
	
}
