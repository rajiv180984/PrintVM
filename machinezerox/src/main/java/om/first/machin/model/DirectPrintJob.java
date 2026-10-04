package om.first.machin.model;

import java.io.Serializable;

public class DirectPrintJob implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	
	private String printJobId;
	private String machineId;
	private String emailId;
	private String localFilePath;
	private String printStatus;
	private String pxprn;
	private String printParam;
	private String requestTime;
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
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getLocalFilePath() {
		return localFilePath;
	}
	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}
	public String getPrintStatus() {
		return printStatus;
	}
	public void setPrintStatus(String printStatus) {
		this.printStatus = printStatus;
	}
	public String getPxprn() {
		return pxprn;
	}
	public void setPxprn(String pxprn) {
		this.pxprn = pxprn;
	}
	public String getPrintParam() {
		return printParam;
	}
	public void setPrintParam(String printParam) {
		this.printParam = printParam;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	@Override
	public String toString() {
		return "DirectPrintJob [printJobId=" + printJobId + ", emailId=" + emailId + ", localFilePath=" + localFilePath
				+ ", printStatus=" + printStatus + ", pxprn=" + pxprn + ", printParam=" + printParam + ", requestTime="
				+ requestTime + "]";
	}
	
	
	

}

//CREATE TABLE IF NOT EXISTS DirectPrint (printJobID TEXT PRIMARY KEY, localPath text NOT NULL,printStatus text NOT NULL, pxprn text NOT NULL, requestTime text NOT NULL, printParam text NOT NULL, cost  REAL );
