package com.first.test.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "PrintDocRequest")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class PrintDocRequest extends Domain {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = -7050972001315548746L;

	// OrderId for Payment related activity
	private String orderId;
	
	// Printjob Id for identifier for Printing activity and persist 
	// PrintIdJob will be created at device in case of direct share
	// but in case of Remote It may be created at backend 
	@NotNull
	@Indexed(unique=true)
	private String printJobId;
	
	@NotNull
	//@Indexed
	private String machineId;
	
	// Document name with extension 
	
	private String docName;
	
	private String custId;
	
	// S3 Bucket URL
	private String documentURL;
	
	private String mobileNo;
	
	private String emailId;
	private String printSecurity;
	
	// Print properties page number, orientation, colour , print pattern
	PrintProperties printProperties;
	
	// Create at Cliet side some random alphanumeric string
	private String securityCode;
	
	
	private double printCost;
	
	private Timestamp printRequestTime;
	
	private String printStatus;
	
	private String printErrorCode;
	
	private String refundStatus;
	
	
	//private ZeroxPaymentDetails paymentDetails;
	
	private String promocode;
	
	private String paymentStatus;
	
	private float balanceAmountToPay;
	
	private String printDate;
	
	//private ThirdPartyPaymentDetails thirdPartyPaymentDetails;
	
	private long createOn;
	
	private String requestType;
	
	// Zerox or Partner If Zerox the user print himself or Partner will issue print command.
	private String businessType;
	
	private String whoPrint;
	
	private String sharedForEmail;
	private String sharedForMobile;
	
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	
	public String getPrintJobId() {
		return printJobId;
	}

	public void setPrintJobId(String printJobId) {
		this.printJobId = printJobId;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	
	
	public String getPrintDate() {
		if(printDate == null) {
			
			Timestamp stamp = new Timestamp(getCreateOn());
			Date date = new Date(stamp.getTime());
			DateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			String d = f.format(date);
			return d;
		}
		return printDate;
	}

	public void setPrintDate(String printDate) {
		if(printDate == null || this.printDate == null) {
		Timestamp stamp = new Timestamp(getCreateOn());
		Date date = new Date(stamp.getTime());
		DateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String d = f.format(date);
		this.printDate = d;
		}
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

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityStr) {
		this.securityCode = securityStr;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public double getPrintCost() {
		return printCost;
	}

	public void setPrintCost(double printCost) {
		this.printCost = printCost;
	}

	public Timestamp getPrintRequestTime() {
		return printRequestTime;
	}

	public void setPrintRequestTime(Timestamp printRequestTime) {
		this.printRequestTime = printRequestTime;
	}

	public String getPrintStatus() {
		return printStatus;
	}

	public void setPrintStatus(String printStatus) {
		this.printStatus = printStatus;
	}

	public String getPrintErrorCode() {
		return printErrorCode;
	}

	public void setPrintErrorCode(String printErrorCode) {
		this.printErrorCode = printErrorCode;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	/*public ZeroxPaymentDetails getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(ZeroxPaymentDetails paymentDetails) {
		this.paymentDetails = paymentDetails;
	}
*/
	public String getDocumentURL() {
		return documentURL;
	}

	public void setDocumentURL(String documentURL) {
		this.documentURL = documentURL;
	}

	
	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getPrintSecurity() {
		return printSecurity;
	}

	public void setPrintSecurity(String printSecurity) {
		this.printSecurity = printSecurity;
	}

	public PrintProperties getPrintProperties() {
		return printProperties;
	}

	public void setPrintProperties(PrintProperties printProperties) {
		this.printProperties = printProperties;
	}

	
	public String getPromocode() {
		return promocode;
	}

	public void setPromocode(String promocode) {
		this.promocode = promocode;
	}

	
	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	

	public float getBalanceAmountToPay() {
		return balanceAmountToPay;
	}

	public void setBalanceAmountToPay(float balanceAmountToPay) {
		this.balanceAmountToPay = balanceAmountToPay;
	}
	
	
/*
	public ThirdPartyPaymentDetails getThirdPartyPaymentDetails() {
		return thirdPartyPaymentDetails;
	}

	public void setThirdPartyPaymentDetails(ThirdPartyPaymentDetails thirdPartyPaymentDetails) {
		this.thirdPartyPaymentDetails = thirdPartyPaymentDetails;
	}
*/
	public long getCreateOn() {
		
		return createOn;
	}

	public void setCreateOn(long createOn) {
		this.createOn = System.currentTimeMillis();
	}

	
	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	
	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	
	public String getWhoPrint() {
		return whoPrint;
	}

	public void setWhoPrint(String whoPrint) {
		if(whoPrint == null) {
			this.whoPrint = "SELF";
		}
		this.whoPrint = whoPrint;
	}
	
	
	public String getSharedForEmail() {
		return sharedForEmail;
	}

	public void setSharedForEmail(String sharedForEmail) {
		this.sharedForEmail = sharedForEmail;
	}

	
	public String getSharedForMobile() {
		return sharedForMobile;
	}

	public void setSharedForMobile(String sharedForMobile) {
		this.sharedForMobile = sharedForMobile;
	}

	@Override
	public String toString() {
		return "PrintDocRequest [orderId=" + orderId + ", printJobId=" + printJobId + ", machineId=" + machineId
				+ ", docName=" + docName + ", custId=" + custId + ", documentURL=" + documentURL + ", mobileNo="
				+ mobileNo + ", emailId=" + emailId + ", printSecurity=" + printSecurity + ", printProperties="
				+ printProperties + ", securityCode=" + securityCode + ", printCost=" + printCost
				+ ", printRequestTime=" + printRequestTime + ", printStatus=" + printStatus + ", printErrorCode="
				+ printErrorCode + ", refundStatus=" + refundStatus + ", promocode=" + promocode + ", paymentStatus="
				+ paymentStatus + ", balanceAmountToPay=" + balanceAmountToPay + ", createOn=" + createOn
				+ ", requestType=" + requestType + ", businessType=" + businessType + ", whoPrint=" + whoPrint + "]";
	}
	
	

	

	
	
	
}
