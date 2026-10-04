package com.first.machin.utils;

import om.first.machin.model.PrintDocRequest;
import om.first.machin.model.PrintJobStatusUpdate;
import om.first.machin.model.RefundStatus;

public class CommonUtility {

	public static PrintJobStatusUpdate createUplodatePayload(PrintDocRequest printDocRequest,
			String errMSG, String printCode, String printJobStatus) {
		// TODO Auto-generated method stub
		PrintJobStatusUpdate jobUpdate = new PrintJobStatusUpdate();
		jobUpdate.setMachineId(printDocRequest.getMachineId());
		jobUpdate.setPrintJobId(printDocRequest.getPrintJobId());
		jobUpdate.setCauseOfFailure(errMSG);
		jobUpdate.setPrintCode(printCode);
		jobUpdate.setPrintJobStatus(printJobStatus);
		jobUpdate.setRefundInitiate(RefundStatus.INIT.name());
		return jobUpdate;
	}

}
