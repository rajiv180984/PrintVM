package com.first.machin.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.first.machin.asyn.ScheduleBulkJob;
import com.first.machin.persistence.PersistRequestLocalService;
import com.first.machin.print.PrintServiceUtility;
import com.first.machin.utils.PrintRequestType;
import com.first.machin.utils.S3BucketAccess;

import om.first.machin.model.BusinessType;
import om.first.machin.model.PartnerItemRequest;
import om.first.machin.model.PrintDocRequest;
import om.first.machin.model.PrintProperties;

@Service
public class PrintServiceHandlerService {

	@Value("${machine.localbasepath}")
	private String localBaseLocation;

	@Value("${machine.os.window}")
	private boolean isWindow;

	@Autowired
	private ScheduleBulkJob scheduleBulkJob;

	@Autowired
	PersistRequestLocalService persistRequestLocalService;

	@Autowired
	S3BucketAccess s3BucketAccess;

	@Autowired
	PrintServiceUtility printServiceUtility;

	public String printProcessRecord(PrintDocRequest printDocRequest) {
		System.out.println(" Print Print Request =========" + printDocRequest.toString());

		String fileLocalPath = null;
		// persist records in SQLLit
		try {
		String localDocURL = "test.pdf"; //getLocalURLForPrintDoc(printDocRequest);
		System.out.println(" Local URL =========== " + localDocURL);
		persistRequestLocalService.persistLocally(printDocRequest, localDocURL);

		/*if (true) {
			return;
		}
		*/
		System.out.println(" printProcessRecord :"+printDocRequest.getBusinessType());
		if (printDocRequest.getBusinessType().equals(BusinessType.PARTNER.name())) {
			try {
				
				// download locally, persiste record in sqllite, 
				fileLocalPath = s3BucketAccess.downloadFile(printDocRequest.getDocumentURL(), localDocURL);
				return "READY_TO_PRINT_BY_PARTNER";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
			}
		}
		// TO-DO Not Feasible condition
		if (printDocRequest.getRequestType().equals(PrintRequestType.REMOTE.name())) {
			try {
				// To Do
				// scheduleBulkJob.startBacthJob(printDocRequest);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// This code must be removed when WIFI_direct implemented
		if (printDocRequest.getRequestType().equals(PrintRequestType.DIRECT.name())) {
			// Create Thread on Completion of download
			// On completion of Download Create Print job
			fileLocalPath = s3BucketAccess.downloadFile(printDocRequest.getDocumentURL(), localDocURL);

			// On recieve from from Mobile from Wifi-Direct Create local path
			
			// create Print Job
			if (fileLocalPath != null || !fileLocalPath.isEmpty()) {
				PrintProperties printProperties = printDocRequest.getPrintProperties();

				// print the job send
				/*printServiceUtility.printJobCreator(fileLocalPath, printProperties.getNumberOfCopies(),
						printProperties.getPrintSide(), printProperties.isCollate(), printProperties.getPrintPageRange(),
						printProperties.isColoured(), printProperties.getPrintQuality());*/
				
				printServiceUtility.printJobCreator(fileLocalPath, printDocRequest);
				
				// update status local
				// update status in central service
				updatePrintJobStatus(printDocRequest);
				return "COMPLETED";
			}

		}		
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "FAIL";
	}

	private void updatePrintJobStatus(PrintDocRequest printDocRequest) {
		// TODO Auto-generated method stub
		updatePrintStatusLocal(printDocRequest);
		updatePrintStatusCenteralServer(printDocRequest);
	}

	private void updatePrintStatusCenteralServer(PrintDocRequest printDocRequest) {
		// TODO Auto-generated method stub

	}

	private void updatePrintStatusLocal(PrintDocRequest printDocRequest) {
		// TODO Auto-generated method stub

	}

	/*private String getLocalURLForPrintDoc(PrintDocRequest printDocRequest) {
		// TODO Auto-generated method stub
		
		String sb = localBaseLocation;
		String path = null;
		if (isWindow) {
			 path = sb.replace('/', '\\');
			System.out.println(" Path where file locally stored " + path);
			
		}else {
			path = sb;
		}

		// create directory if not exist
		path = createIfFolderDoesExist(path);
		
		// create Complete local file path
		// basePath/machineId/mobileNo-docId
		if (isWindow) {
			return path.concat("\\").concat(printDocRequest.getMobileNo()).concat("-").concat(printDocRequest.getDocId());
		}else {
			return path.concat("/").concat(printDocRequest.getMobileNo()).concat("-").concat(printDocRequest.getDocId());
		}
		
	}
*/
	// Partner ID Get the Doc and create print job by Doc Id
	public void createPrintJObFromPartnerCommand(PartnerItemRequest partnerItemRequest) {
		PrintDocRequest printDoc = persistRequestLocalService.createPrintJObFromPartnerCommand(partnerItemRequest);
		System.out.println(" Printrequest From Partner :"+ printDoc.toString() );
		// check file exist locally
		String fileLocalPath = null;
		File file = new File(printDoc.getLocalPath());
		if (file.exists()) {
			
			fileLocalPath = printDoc.getLocalPath();
			System.out.println("Partner File Exist "+fileLocalPath);
		} else {
			fileLocalPath = s3BucketAccess.downloadFile(printDoc.getDocumentURL(), fileLocalPath);
		}

		PrintProperties printProperties = printDoc.getPrintProperties();

		printServiceUtility.printJobCreator(fileLocalPath, printDoc);

	}

	public void remotePrintPersist(PrintDocRequest printDocRequest) {
		System.out.println(" Print Print Request =========" + printDocRequest.toString());

		String fileLocalPath = null;
		// persist records in SQLLit
		String localDocURL = "Test.pdf"; //getLocalURLForPrintDoc(printDocRequest);
	    System.out.println(" Local URL =========== " + localDocURL);
	    
		//localDocURL = "C:\\Zerox\\9988551218";
		// persistRequestLocalService.persistLocally(printDocRequest, localDocURL);

		if (printDocRequest.getRequestType().equals(PrintRequestType.REMOTE.name())) {
			try {
				
				
				localDocURL = s3BucketAccess.downloadFile(printDocRequest.getDocumentURL(), localDocURL);
				persistRequestLocalService.persistLocally(printDocRequest, localDocURL);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// This code must be removed when WIFI_direct implemented

	}

	private String createIfFolderDoesExist(String localDocURL) {
		
		try {
		File tmpDir = new File(localDocURL);
		if(! tmpDir.exists()) {
			tmpDir.mkdir();
			return localDocURL;
		}
		}catch (Exception e) {
			return localDocURL;
		}
		
		return localDocURL;
		
	}

}
