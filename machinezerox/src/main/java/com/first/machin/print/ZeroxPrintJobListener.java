package com.first.machin.print;

import java.util.Set;

import javax.print.attribute.standard.PrinterStateReason;
import javax.print.attribute.standard.PrinterStateReasons;
import javax.print.attribute.standard.Severity;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.first.machin.persistence.PersistRequestLocalService;
import com.first.machin.service.RequestStatusUpdateService;

import om.first.machin.model.PrintJobStatusUpdate;
import javax.print.event.PrintJobAdapter;

@Service
public class ZeroxPrintJobListener extends  PrintJobAdapter {

	@Autowired
	RequestStatusUpdateService requestStatusUpdateService;

	@Autowired
	PersistRequestLocalService persistRequestLocalService;

	@Override
	public void printDataTransferCompleted(PrintJobEvent pje) {
		// TODO Auto-generated method stub
		System.out.println(" Print job transfered");

	}

	@Override
	public void printJobCompleted(PrintJobEvent pje) {
		//
		  System.out.println(" Hello this is compleed ========");
	        synchronized (ZeroxPrintJobListener.this) {
	          System.out.println(" Hello this is compleed ========1");
	          ZeroxPrintJobListener.this.notify();
	        }
		System.out.println(" Print job Complted");
		// update status in Local DB
		// Update status to Cloud

		getLocalandCloudUpdateOfPrintJob(pje);
	}

	@Override
	public void printJobFailed(PrintJobEvent pje) {
		System.out.println(" Print job failed =========");
		getLocalandCloudUpdateOfPrintJob(pje);

	}

	@Override
	public void printJobCanceled(PrintJobEvent pje) {
		// TODO Auto-generated method stub
		getLocalandCloudUpdateOfPrintJob(pje);
	}

	@Override
	public void printJobNoMoreEvents(PrintJobEvent pje) {
		// TODO Auto-generated method stub
		System.out.println(" This is PrinterJOb");

	}

	@Override
	public void printJobRequiresAttention(PrintJobEvent pje) {
		// TODO Auto-generated method stub

	}

	private void getLocalandCloudUpdateOfPrintJob(PrintJobEvent pje) {

		PrintJobStatusUpdate printJobStatusUpdate = getJobStatus(pje);
		// update the set
		requestStatusUpdateService.updateRequestStatusOnCloud(printJobStatusUpdate);
		persistRequestLocalService.persistStatusInLocalDB(printJobStatusUpdate);
	}

	private PrintJobStatusUpdate getJobStatus(PrintJobEvent pje) {
		// TODO Auto-generated method stub
		PrinterStateReasons psr = pje.getPrintJob().getPrintService().getAttribute(PrinterStateReasons.class);
	    if (psr != null) {
	      Set<PrinterStateReason> warning = psr.printerStateReasonSet(Severity.WARNING);
	      Set<PrinterStateReason> errors = psr.printerStateReasonSet(Severity.ERROR);
	      for (PrinterStateReason reason : errors)
	        System.out.printf(" Reason : %s",reason.getName());
	      System.out.println();
	    }
		return null;
	}

}
