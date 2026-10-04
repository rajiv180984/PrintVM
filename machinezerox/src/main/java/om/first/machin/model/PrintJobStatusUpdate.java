package om.first.machin.model;

import java.io.Serializable;

public class PrintJobStatusUpdate implements Serializable {

	private static final long serialVersionUID = 3105356426199854966L;

	private String printJobId;
	private String machineId;
	private String printJobStatus;
	private String causeOfFailure;
	private String printCode;
	
	private String refundInitiate;
    private boolean cloudNotified;
    
    
	public boolean isCloudNotified() {
		return cloudNotified;
	}

	public void setCloudNotified(boolean cloudNotified) {
		this.cloudNotified = cloudNotified;
	}

	public String getPrintJobId() {
		return printJobId;
	}

	public void setPrintJobId(String printJobId) {
		this.printJobId = printJobId;
	}

	
	public String getPrintCode() {
		return printCode;
	}

	public void setPrintCode(String printCode) {
		this.printCode = printCode;
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
	
	
	
}
