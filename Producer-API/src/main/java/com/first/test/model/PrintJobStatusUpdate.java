package com.first.test.model;

import java.io.Serializable;

public class PrintJobStatusUpdate implements Serializable {

	private static final long serialVersionUID = 3105356426199854966L;

	private String printJobId;
	private String machineId;
	private String printJobStatus;
	private String causeOfFailure;
	
	private String refundInitiate;
	
	private int paperCount;
	

	public String getPrintJobId() {
		return printJobId;
	}

	public void setPrintJobId(String printJobId) {
		this.printJobId = printJobId;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getPrintJobStatus() {
		return printJobStatus;
	}

	public void setPrintJobStatus(String printJobStatus) {
		this.printJobStatus = printJobStatus;
	}

	public String getCauseOfFailure() {
		return causeOfFailure;
	}

	public void setCauseOfFailure(String causeOfFailure) {
		this.causeOfFailure = causeOfFailure;
	}

	public String getRefundInitiate() {
		return refundInitiate;
	}

	public void setRefundInitiate(String refundInitiate) {
		this.refundInitiate = refundInitiate;
	}

	public int getPaperCount() {
		return paperCount;
	}

	public void setPaperCount(int paperCount) {
		this.paperCount = paperCount;
	}
	
	
	
}
