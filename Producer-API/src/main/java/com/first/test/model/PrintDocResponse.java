package com.first.test.model;

import java.io.Serializable;

public class PrintDocResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -393650721822084333L;
    
	private String docid;
	private String docName;
	private String requestTimestamp;
	//doc path from S3 Bucket // read access
	private String docPath;
	private String printSecurityCode;
	private String machineId;
	private GeoLocation machineLocation;
	private String printCost;
	
	public String getDocid() {
		return docid;
	}
	public void setDocid(String docid) {
		this.docid = docid;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getRequestTimestamp() {
		return requestTimestamp;
	}
	public void setRequestTimestamp(String requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}
	public String getDocPath() {
		return docPath;
	}
	public void setDocPath(String docPath) {
		this.docPath = docPath;
	}
	public String getPrintSecurityCode() {
		return printSecurityCode;
	}
	public void setPrintSecurityCode(String printSecurityCode) {
		this.printSecurityCode = printSecurityCode;
	}
	public String getMachineId() {
		return machineId;
	}
	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}
	public GeoLocation getMachineLocation() {
		return machineLocation;
	}
	public void setMachineLocation(GeoLocation machineLocation) {
		this.machineLocation = machineLocation;
	}
	public String getPrintCost() {
		return printCost;
	}
	public void setPrintCost(String printCost) {
		this.printCost = printCost;
	}
	

}

