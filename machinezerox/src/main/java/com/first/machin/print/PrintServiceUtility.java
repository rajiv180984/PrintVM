package com.first.machin.print;

import java.io.FileInputStream;
import java.sql.Ref;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.print.CancelablePrintJob;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintJobAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Fidelity;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.MediaTray;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.PrintQuality;
import javax.print.attribute.standard.PrinterIsAcceptingJobs;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.SheetCollate;
import javax.print.attribute.standard.Sides;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.first.machin.exception.MachineExcepion;
import com.first.machin.persistence.PersistRequestLocalService;
import com.first.machin.service.PrintJobCallable;
import com.first.machin.service.RequestStatusUpdateService;
import com.first.machin.utils.ErrorCode;
import com.first.machin.utils.MachineConstant;
import com.first.machin.utils.PrintJobStatusCode;

import om.first.machin.model.PrintDocRequest;
import om.first.machin.model.PrintJobStatusUpdate;
import om.first.machin.model.PrintProperties;
import om.first.machin.model.PrintStatus;
import om.first.machin.model.RefundStatus;

import java.util.concurrent.TimeUnit;  

@Service
public class PrintServiceUtility {
	private Logger logger = LoggerFactory.getLogger(PrintServiceUtility.class);
	// to-do Remove it is for testing
	@Value("${print.thread.timeout.seconds}")
	private long printThreadTimeout;
	
	
	@Autowired
	ThreadPoolSingleton threadPool;
	
	@Autowired
	RequestStatusUpdateService requestStatusUpdateService;

	// to-do Remove it is for testing
	@Autowired
	PersistRequestLocalService persistRequestLocalService;


	@Value("${printername}")
	private String printerName; 
	
	@Value("${print.thresold}")
	private long printWaitThreadSold; 
	
	
	@Autowired
	ZeroxPrintJobListener zeroxPrintJobListener; 

	/*@Autowired 
	ZeroxPrintServiceAttributeListener zeroxPrintServiceAttributeListener;
	*/
	@Autowired
	PrinterHeartBeat printerHeartBeat;
	
	private PrintService printService;
	
	/*
	 * If Default printer not working
	 */
	
	private Runtime runtime = Runtime.getRuntime();
	
	private PrintService getPrintService(String printerName) {
		
		
		 DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
		  PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
		 // patts.add(Sides.DUPLEX);
		  PrintService[] ps = PrintServiceLookup.lookupPrintServices(flavor, patts);
		  if (ps.length == 0) {
		    throw new IllegalStateException("No Printer found");
		  }
		  System.out.println("Available printers: " + Arrays.asList(ps));

		  PrintService myService = null;
		  for (PrintService printService : ps) {
			  //"HL-L2320D-series"
		    if (printService.getName().contains(printerName)) {
		      myService = printService;
		      break;
		    }
		  }

		  if (myService == null) {
		    throw new IllegalStateException("Printer not found");
		  }
		  return myService;
	}
	
	
	
	
	/*
	 * default values numberOfCopies =1; printSide = DOUBLE SIDED isCollate = true, Colour = B&W, printQuality=Normal
	 * 
	 */

	public Map<String, String> printJobCreator(String filePath, PrintDocRequest printDocRequest) {
		System.out.println(" ====== Start Print Job =========");
		System.out.println("Prinater Name:  "+ printerName);
		Map<String, String> responseMap = new HashMap<String, String>();
		PrintProperties printProps = printDocRequest.getPrintProperties();

		printService = findPrintService(printService);
		// Add PrinterLevel Atribute Listner
		printService.addPrintServiceAttributeListener(new ZeroxPrintServiceAttributeListener());
		// used for debuging
		//checkAttributeForDebuge(printService);

		try {
			// read file from Folder or AWS S3
			FileInputStream fileInputStream = new FileInputStream(filePath);
			if (fileInputStream == null) {
				System.out.println(" Incorrect File");
				responseMap.put(MachineConstant.errorCode, ErrorCode.M104.name());
				responseMap.put(MachineConstant.errorMSG, ErrorCode.M104.getErrorMSG());
				throw new MachineExcepion( ErrorCode.M104.getErrorMSG());

			}
			// Set the document type
			DocFlavor myFormat = DocFlavor.INPUT_STREAM.PDF;
			// Create a Doc
			Doc myDoc = new SimpleDoc(fileInputStream, myFormat, null);
			// Build a set of attributes
			DocAttributeSet das = new HashDocAttributeSet();
			// set Print command attribute 
			PrintRequestAttributeSet aset = setPrintAttributes(printProps);
			
			
			/* Highend printer support below code To-Do If Machine Stopped then notify to system 
			String printerState = printService.getAttributes().get(PrinterState.class).toString();
			if (printerState.equals(PrinterState.STOPPED.toString())) {
				// notify to admin
			}*/
			
			try {
				DocPrintJob job = printService.createPrintJob();
				// Print JOb Listner
				job.addPrintJobListener(zeroxPrintJobListener);
				// threading
				Future<PrintJobAttributeSet> future = threadPool.getExecutorInstance().submit(
						new PrintJobCallable(job, myDoc, aset));
				PrintJobAttributeSet printJobAttributeSet = future.get();
				//PrintJobAttributeSet printJobAttributeSet = future.get(printThreadTimeout, TimeUnit.SECONDS);
				if(printJobAttributeSet == null) {
					future.cancel(true);
				}
				
				// check it is done
				if(printJobAttributeSet!=null && future.isDone()) {
					String command = "lpstat -p "+printerName;
					boolean status = printerHeartBeat.checkPrinerIsUnavailable(runtime, printerName, command, printDocRequest.getMachineId());
					// check listner attributes and decide it is printing successfully
					if(!status) {
						cancelPrintUpdateforRefund(printDocRequest, job);
						
					}else {
						 updatePrintJobStatus(printDocRequest, PrintStatus.COMPLETE.name());
					}
			
				}else {
					// 
					updatePrintJobStatus(printDocRequest, PrintStatus.FAIL.name());
				}
				
			} catch (Exception pe) {
				pe.printStackTrace();
			}
	
		} catch (Exception e) {
			logger.error(" Failed to Print ", e);
			//FIXME Call Update Status local and Cloud 
		}
		return responseMap;
	}

	private void cancelPrintUpdateforRefund(PrintDocRequest printDocRequest, DocPrintJob job) throws PrintException {
		System.out.println("======== JOB Are cancel ==================");
		// cancel print job
		CancelablePrintJob cancelPrintJob = (CancelablePrintJob)job;
		updatePrintJobStatus(printDocRequest, PrintStatus.FAIL.name());
		
		System.out.println("************** :"+(PrinterIsAcceptingJobs)(job.getPrintService().getAttribute(PrinterIsAcceptingJobs.class)));
		PrinterIsAcceptingJobs printerIsAcceptingJobs = (PrinterIsAcceptingJobs)(job.getPrintService().getAttribute(PrinterIsAcceptingJobs.class));
		
		System.out.println("**********::"+printerIsAcceptingJobs.getValue());
		if ((PrinterIsAcceptingJobs)(job.getPrintService().getAttribute(
		          PrinterIsAcceptingJobs.class)) ==
		          PrinterIsAcceptingJobs.NOT_ACCEPTING_JOBS) {
			System.out.println(" This is cancel job ***********************");
			cancelPrintJob.cancel();	
		}
		
		System.out.println(" JOB Are cancel ==================");
		// call cups cancel for all jobs 
		String cancelCommand = "lpstat -W not-completed -o";
		boolean isCancel = printerHeartBeat.cancelJobByJobName(runtime, cancelCommand);
		if(isCancel) {
			// update status on cloud
			PrintJobStatusUpdate printJobStatusUpdate = new PrintJobStatusUpdate();
			printJobStatusUpdate.setMachineId(printDocRequest.getMachineId());
			printJobStatusUpdate.setPrintJobId(printDocRequest.getPrintJobId());
			printJobStatusUpdate.setPrintJobStatus(PrintStatus.FAIL.name());
			// cause
			printJobStatusUpdate.setCauseOfFailure("Printer Offline");
			printJobStatusUpdate.setPrintCode(PrintJobStatusCode.PRINTER_OFFLINE);
			// refund amount
			printJobStatusUpdate.setRefundInitiate(RefundStatus.INIT.name());
			
			requestStatusUpdateService.updateRequestStatusOnCloud(printJobStatusUpdate);
			persistRequestLocalService.persistStatusInLocalDB(printJobStatusUpdate);
			
		}
		
		// 
	}




	private void checkAttributeForDebuge(PrintService printService) {
		PrintServiceAttributeSet atset = printService.getAttributes();
		
		for (Attribute att : atset.toArray()) {
			System.out.println(" Att :" + att.getName());
		}
	}




	private PrintService findPrintService(PrintService printService) {
		//PrintServiceLookup.lookupDefaultPrintService();
		//PrintServiceLookup.lookupMultiDocPrintServices(flavors, attributes)
		if(printService == null) {
			printService = getPrintService(printerName);
			
		}else {
			System.out.println(" Printservices is null" + printService.toString());
		}
		if(printService == null) {
			// send notification to Server throw mqtt to notify cartaker 
			// stop to accept request notify mobile
			throw new MachineExcepion("PrinterNotfound", 101);
		}
		return printService;
	}




	private void updatePrintJobStatus(PrintDocRequest printDocRequest, String status) {
		PrintJobStatusUpdate printJobStatusUpdate = new PrintJobStatusUpdate();
		printJobStatusUpdate.setPrintJobId(printDocRequest.getPrintJobId());
		printJobStatusUpdate.setPrintJobStatus(status);
		printJobStatusUpdate.setMachineId(printDocRequest.getMachineId());
		printJobStatusUpdate.setRefundInitiate(RefundStatus.INIT.name());
		requestStatusUpdateService.updateRequestStatusOnCloud(printJobStatusUpdate);
		persistRequestLocalService.persistStatusInLocalDB(printJobStatusUpdate);
	}

	private void dummyTestingUpdatePrintStatus(PrintDocRequest printDocRequest) {
		logger.info(" Update Print Job Status");
		PrintJobStatusUpdate printUpdate = new PrintJobStatusUpdate();
		printUpdate.setPrintJobId(printDocRequest.getPrintJobId());
		printUpdate.setMachineId(printDocRequest.getMachineId());
		printUpdate.setPrintJobStatus("DONE");
		printUpdate.setRefundInitiate("NA");
		//FIXME uncomment both
		//requestStatusUpdateService.updateRequestStatusOnCloud(printUpdate);
		persistRequestLocalService.persistStatusInLocalDB(printUpdate);
	}

	private PrintRequestAttributeSet setPrintAttributes(PrintProperties printProp) {
			System.out.println(" Print Properies :"+printProp.toString());
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		
		// number of copies
		if (printProp.getNumberOfCopies() == 0 || printProp.getNumberOfCopies() < 0) {
			aset.add(new Copies(1));
		}else {
			aset.add(new Copies(printProp.getNumberOfCopies()));
		}
		
		//page size
		aset.add(MediaSizeName.ISO_A4);
		// side of print
		if(printProp.getPrintSide() == null) {
			 aset.add(Sides.ONE_SIDED);
		}else {
		 Sides side = getSide(printProp.getPrintSide());
		 aset.add(side);
		}
		
	
		// Sheet collation 2-copy collated job will print as (1,2,3,1,2,3), Uncollate:  2-copy uncollated job will be printed as (1,1,2,2,3,3).
		if(printProp.isCollate()) {
			aset.add(SheetCollate.COLLATED);	
		}else {
			aset.add(SheetCollate.UNCOLLATED);
		}
		
		// Print Job Should be not be rejected
		aset.add(Fidelity.FIDELITY_FALSE);
		
		// Check This is feature is not supported by all printer
		//aset.add(MediaTray.MAIN);
		
		// To-DO Research for orientation 
		if(printProp.getOrientation()!=null && printProp.getOrientation().equalsIgnoreCase(OrientationRequested.PORTRAIT.getName())) {
		 aset.add(OrientationRequested.PORTRAIT);
		}else if(printProp.getOrientation()!=null && printProp.getOrientation().equalsIgnoreCase(OrientationRequested.LANDSCAPE.getName())) {
			aset.add(OrientationRequested.LANDSCAPE);
		}else {
			aset.add(OrientationRequested.PORTRAIT);
		}
		// Page Ranges
		System.out.println(" Page Expression :"+printProp.getPexpn());
			PageRanges pageRange = new PageRanges(printProp.getPexpn());
			aset.add(pageRange);
		
		// Is collored 
		if(! printProp.isColor()) {
		aset.add(Chromaticity.MONOCHROME);
		}else {
			// is disabled feature
			aset.add(Chromaticity.COLOR);
		}
		
		// Add Print quality
		PrintQuality printQualityValue = PrintAttributeUtility.evaluatePrintQuality(printProp.getPrintQuality());
		aset.add(printQualityValue);
		return aset;
	}

	private Sides getSide(String printSide) {
		if(printSide == null) {
			return Sides.ONE_SIDED;
		}
		if (printSide.equals(PrintSide.ONE_SIDED.name())) {
			return Sides.ONE_SIDED;

		} else if (printSide.equals(PrintSide.DUPLEX.name())) {
			return Sides.DUPLEX;
		} else if (printSide.equals(PrintSide.TUMBLE.name())) {
			return Sides.TUMBLE;

		} else if (printSide.equals(PrintSide.TWO_SIDED_LONG_EDGE.name())) {
			return Sides.TWO_SIDED_LONG_EDGE;
		} else if (printSide.equals(PrintSide.TWO_SIDED_SHORT_EDGE.name())) {
			return Sides.TWO_SIDED_SHORT_EDGE;
		}

		return Sides.ONE_SIDED;

	}

}
