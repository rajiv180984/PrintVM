package om.first.machin.model;

import java.io.Serializable;
import java.sql.Timestamp;



import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class PrintDocRequest extends Domain {
  
	/**
	 * 
	 */
	private static final long serialVersionUID = -4507692825956544700L;

	private String printJobId;
	
	private String machineId;;
	
	// Document name with extension 
	private String MobileNo;
	
	private String docName;
	
	private String documentURL;
	
	private String emailId;
	private String localPath;
	
	private String printSecurityKey;
	
	// Print properties page number, orientation, colour , print pattern
	PrintProperties printProperties;
	
	// Create at Cliet side some random alphanumeric string
	private String securityCode;
	
	private float printCost;
	
	private long printRequestTime;
	
	private String printStatus;
	
	private String printErrorCode;
	
	private String requestType;
	
	// Zerox or Partner
	private String businessType;
	
	private int pageCount;

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

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	
	public String getMobileNo() {
		return MobileNo;
	}

	public void setMobileNo(String mobileNo) {
		MobileNo = mobileNo;
	}

	public String getDocumentURL() {
		return documentURL;
	}

	public void setDocumentURL(String documentURL) {
		this.documentURL = documentURL;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public String getPrintSecurityKey() {
		return printSecurityKey;
	}

	public void setPrintSecurityKey(String printSecurityKey) {
		this.printSecurityKey = printSecurityKey;
	}

	public PrintProperties getPrintProperties() {
		return printProperties;
	}

	public void setPrintProperties(PrintProperties printProperties) {
		this.printProperties = printProperties;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public float getPrintCost() {
		return printCost;
	}

	public void setPrintCost(float printCost) {
		this.printCost = printCost;
	}

	
	public long getPrintRequestTime() {
		return printRequestTime;
	}

	public void setPrintRequestTime(long printRequestTime) {
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
	
	

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	@Override
	public String toString() {
		return "PrintDocRequest [printJobId=" + printJobId + ", machineId=" + machineId + ", docName=" + docName
				+ ", documentURL=" + documentURL + ", emailId=" + emailId + ", localPath=" + localPath
				+ ", printSecurityKey=" + printSecurityKey + ", printProperties=" + printProperties + ", securityCode="
				+ securityCode + ", printCost=" + printCost + ", printRequestTime=" + printRequestTime
				+ ", printStatus=" + printStatus + ", printErrorCode=" + printErrorCode + ", requestType=" + requestType
				+ ", businessType=" + businessType + "]";
	}
	
	
	
	
}
