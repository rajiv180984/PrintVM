package com.first.machin.service;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.first.machin.exception.NoRecordFoundException;
import com.first.machin.machinezerox.MachineWIFIController;
import com.first.machin.persistence.SQLLiteConnection;
import com.first.machin.persistence.SQLLiteQuerisBuilder;
import com.first.machin.print.PrintServiceUtility;
import com.first.machin.print.ThreadPoolSingleton;
import com.first.machin.utils.AESUtils;
import com.first.machin.utils.ErrorCode;
import com.first.machin.utils.FileWriteUtility;
import com.first.machin.utils.IllegalOperationException;
import com.first.machin.utils.MachineConstant;
import com.first.machin.utils.RequestType;

import om.first.machin.model.PrintDocRequest;

@Service
public class MachineServiceImpl implements MachineService {

	@Value("${filetransfer.thread.timeout.seconds}")
	private long filetransferThreadTimeout;
	
	@Autowired
	ThreadPoolSingleton threadPool;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	SQLLiteConnection sQLLiteConnection;
	
	@Autowired
	PrintServiceUtility printServiceUtility;
	
	
	
	@Autowired
	AESUtils aesUtils;
	
	@Value("${machineId}")
	private String machineIdTest;
	
	@Autowired
	private RequestStatusUpdateService requestStatusUpdateService;

	private static Logger logger = LoggerFactory.getLogger(MachineServiceImpl.class);
	
	@Override
	public boolean checkReadiness(String emailId) {
		// TODO Auto-generated method stub
		if (!printerIsReady()) {
			return false;
		}
		if (isUserAttack()) {
			return false;
		}
		return true;
	}

	private boolean isUserAttack() {
		// TODO Auto-generated method stub
		// maintain HashMap
		// Add in hash each user request
		return false;
	}

	private boolean printerIsReady() {
		// TODO Auto-generated method stub
		// FIXME Check Priter is ready or working fine
		return true;
	}

	@Override
	public Map<String, Object> validateRequest(String servercertkey, String printJobId, String printParam, String machineId) {
		// TODO Auto-generated method stub
		//	System.out.println(" ServerKey ======: "+ servercertkey);
		//	servercertkey = servercertkey.trim().replace(" ", "+");

		
		if(!machineIdTest.equals(machineId )) {
		   	throw new IllegalOperationException("Incorrec Machine ");
		}
		
		try {
		// Step 1: Secrete Key configured on Machine
			System.out.println(" This is Machine ID :"+ machineIdTest);
		String secreteKey = aesUtils.getMachineSecuritykey(aesUtils.getMachinekey(),machineId);
		
		String descryptServercertkey = aesUtils.decrypt(servercertkey, "8782");
		System.out.println(" This iS Decrped =====" + descryptServercertkey);
		
		//AESUtils
		// Step 2: read Machine Decryption Key
		// Step 3: Descrpt servercertkey
		// Extract all value by parsing @@@
		// FIXME
		
		//servercertkey = "PrintjboId@@@test@@@20.0@@@5";
		String values[] = descryptServercertkey.split(MachineConstant.DELIM);
		// Compare any value tempered or not
		// If any value tempered reject
		// Else
		// return null;
		// Extract Value from Security String
		System.out.println(values.toString());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("valid", true);
		map.put("cost", Float.parseFloat(values[2]));
		map.put("pageCount", Integer.parseInt(values[2]));
		
		return map;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return null;

	}

	@Override
	public Map<String, String> filereceiver(MultipartFile file, PrintDocRequest printDocRequest) {
          System.out.println(" FIle Reciver Started =============1");
          Map<String, String> responseMap = new HashMap();
		// FIXME Only five people allowed to submit print job.
         
       // Make call to cloud to validate
         printDocRequest = requestStatusUpdateService.getPrintJobDetailsValidate(printDocRequest );
         System.out.println(" Feched result :"+ printDocRequest.toString());
  		 if(printDocRequest == null) {
  			
  			responseMap.put(MachineConstant.errorCode, ErrorCode.T102.name());
			responseMap.put(MachineConstant.errorMSG, ErrorCode.T102.getErrorMSG());
			return responseMap;
  		 }
  		
  		 // check directory exist. Reject if Transacion already exist
		try {
		StringBuilder builder = new StringBuilder();
		builder.append(FileWriteUtility.basePath).append("/").append(printDocRequest.getPrintJobId()).append(".pdf");
		String localFilePath = builder.toString();
		// create file with PrintJobId
		File saveFileByPrintJobId = new File(localFilePath);
		System.out.println(" FIle Reciver Started =============2"+ localFilePath);
		if (saveFileByPrintJobId.exists()) {
			// if file already exist Throw error
			// FIXME
			System.out.println(" Already Eist "+saveFileByPrintJobId.exists());
			
			responseMap.put(MachineConstant.errorCode, ErrorCode.T101.name());
			responseMap.put(MachineConstant.errorMSG, ErrorCode.T101.getErrorMSG());
			return responseMap;
		}
		System.out.println(" FIle Reciver Started =============3");
		printDocRequest.setLocalPath(localFilePath);

		// Persist File in System
		persistInDB(RequestType.DIRECT.name(), printDocRequest);
		
		// Create thread
		RecievPrintJobProcessor printProcessor = new RecievPrintJobProcessor(file, printDocRequest,
				saveFileByPrintJobId);
		Future<Integer> future = threadPool.getExecutorInstance().submit(printProcessor);
		System.out.println(" =====  Future :"+ future.toString());
		int result = future.get(filetransferThreadTimeout, TimeUnit.SECONDS);
		System.out.println(" Future Results :"+result);
		
		if(result != 1) {
			Thread.sleep(filetransferThreadTimeout*10);
			if(!future.isDone()) {
				future.cancel(true);
				responseMap.put(MachineConstant.errorCode, ErrorCode.M103.name());
				responseMap.put(MachineConstant.errorMSG, ErrorCode.M103.getErrorMSG());
				return responseMap;
			}
		}
		if(future.isDone()) {
			// printing job started
			responseMap = printServiceUtility.printJobCreator(printDocRequest.getLocalPath(), printDocRequest);
		}
		}catch (Exception e) {
			logger.error(" Failed to print job ", e);
			responseMap.put(MachineConstant.errorCode, ErrorCode.P101.name());
			responseMap.put(MachineConstant.errorMSG, ErrorCode.P101.getErrorMSG());
		}
		return responseMap;
	}

	private boolean persistInDB(String requestType, PrintDocRequest printDocRequest) {
		System.out.println(" Persist data ==========");
		PrintDocRequest printrequest = new PrintDocRequest();

		try {
			String sql = "insert into DirectPrint(printJobID, localPath, pageCount, printStatus, printParam, cost,requestTime) values(?,?,?,?,?,?,?);";
			/*jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
				@Override
				public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
			*/		Connection conn = sQLLiteConnection.createConnection();
					PreparedStatement ps = conn.prepareStatement(SQLLiteQuerisBuilder.DIRECT_PRINT_INSERT);
					System.out.println(" Persist data 2==========");
					ps.setString(1, printDocRequest.getPrintJobId());
					ps.setString(2, printDocRequest.getLocalPath());
					ps.setInt(3, printDocRequest.getPrintProperties().getPageCount());
					ps.setString(4, "PROCESSING");
					ps.setString(5, "printParam");
					ps.setFloat(6, printDocRequest.getPrintCost());
					ps.setString(7, String.valueOf(System.currentTimeMillis()));

					System.out.println(" Persist data ========== Done");
					ps.execute();
					return true;
				//}
			//});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
		//
	}

	
}
