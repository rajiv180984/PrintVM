package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="PartnerDetails")
public class PartnerDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2774521506530506338L;
	@Id
	private String partnerId;
	private String machineId;
	private String partnerMobileNo;
	private String emailId;
	private String partnerName;
	private String partnerAddress;
	private String cityname;
	private PartnerPaymentDetails partnerPaymentDetails;
	private PartnerPrintStats partnerPrintStats;
	
	
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public void setPartnerAddress(String partnerAddress) {
		this.partnerAddress = partnerAddress;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String parnerId) {
		this.partnerId = parnerId;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	
	
	public String getPartnerAddress() {
		return partnerAddress;
	}
	public PartnerPaymentDetails getPartnerPaymentDetails() {
		return partnerPaymentDetails;
	}
	public void setPartnerPaymentDetails(PartnerPaymentDetails partnerPaymentDetails) {
		this.partnerPaymentDetails = partnerPaymentDetails;
	}
	public PartnerPrintStats getPartnerPrintStats() {
		return partnerPrintStats;
	}
	public void setPartnerPrintStats(PartnerPrintStats partnerPrintStats) {
		this.partnerPrintStats = partnerPrintStats;
	}
	public String getPartnerMobileNo() {
		return partnerMobileNo;
	}
	
	public void setPartnerMobileNo(String partnerMobileNo) {
		this.partnerMobileNo = partnerMobileNo;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	
}
