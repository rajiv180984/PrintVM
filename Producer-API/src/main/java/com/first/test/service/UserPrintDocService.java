package com.first.test.service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.first.test.model.CostDerivative;
import com.first.test.model.Customer;
import com.first.test.model.PrintDocRequest;
import com.first.test.model.PrintJobStatusUpdate;

public interface UserPrintDocService {
	
	PrintDocRequest persistPrintRequest(String machineId, PrintDocRequest printDocRequest, MultipartFile file);
	
	PrintDocRequest persistRemotePrintRequest(String machineId, PrintDocRequest printDocRequest, MultipartFile file);
	
	
	CostDerivative getPrintCost(String machineId);

	void updatePrintJobstatus(PrintJobStatusUpdate printJobStatusUpdate);

	void shareprintdoc(String machineId, String mobileNolist,String mobileOfOwnerOfdoc, MultipartFile file);

	PrintDocRequest printPaymentPendingRequest(String machineId, PrintDocRequest printRequest);
	
	public void publishPrintCommandByOwnerOfDoc(PrintDocRequest printDocRequest);

	List<PrintDocRequest> getPendingPrintRequests(String emailId, String mobileNo);
	
	PrintDocRequest printsharedDoc(PrintDocRequest printDocRequest);

	String getUploadS3URL(String printJobId, String emailId);

	List<PrintDocRequest> printJobHistory(String emailId, String status, String requestType);

	PrintDocRequest remotePrintTrigger(String printjobId, String machineId);

	PrintDocRequest getPrintJobDetails(String machineId, String printJobId);
}
