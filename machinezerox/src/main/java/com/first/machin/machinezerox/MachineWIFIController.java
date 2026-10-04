package com.first.machin.machinezerox;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.machin.service.MachineService;
import com.first.machin.service.RequestStatusUpdateService;
import com.first.machin.utils.CommonUtility;
import com.first.machin.utils.ErrorCode;
import com.first.machin.utils.FileWriteUtility;
import com.first.machin.utils.MachineConstant;
import com.first.machin.utils.RequestType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import om.first.machin.model.GenericResponseEntity;
import om.first.machin.model.PrintDocRequest;
import om.first.machin.model.PrintJobStatusUpdate;
import om.first.machin.model.PrintProperties;
import om.first.machin.model.PrintStatus;

@RestController
@RequestMapping(value = "/first/admin")
public class MachineWIFIController {

	private static Logger logger = LoggerFactory.getLogger(MachineWIFIController.class);

	@Autowired
	Gson gson;
	
	@Autowired
	MachineService machineService;
	
	@Autowired
	RequestStatusUpdateService requestStatusUpdateService;

	@PostMapping(value = "/filereceiver")
	public ResponseEntity<Map<String, String>> getPrintDocRequest(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "machineId", required = false) String machineId,
			@RequestParam(value = "printJobId", required = false) String printJobId,
			@RequestParam(value = "emailId", required = false) String emailId,
			@RequestParam(value = "printParam", required = false) String printParam,
			@RequestParam(value = "servercertkey", required = false) String servercertkey
			) {
		
		System.out.println(" This is reciever 2======"+servercertkey);
		
		Map<String, String> resultMap = new HashMap<>();
		PrintDocRequest printDocRequest = new PrintDocRequest();
		try {
			
			String printRequestStr = URLDecoder.decode(printParam, StandardCharsets.UTF_8.name());
			System.out.println(" JSON String ======"+printRequestStr);
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(printRequestStr).getAsJsonObject();
			JsonObject printProperties = obj.getAsJsonObject("printProperties");
			System.out.println(" This is Print Props 1:"+ printProperties.toString());
			Gson gson = new Gson();
			PrintProperties prinprop = gson.fromJson(printProperties, PrintProperties.class);
			System.out.println(" This is Print Props 1:"+ prinprop.toString());
			JsonObject jsonObject = new JsonParser().parse(printRequestStr).getAsJsonObject();
			String prinCerKey = jsonObject.get("printProperties").getAsJsonObject().get("servercertkey").getAsString();
			//System.out.println(" Parsing cerKey  ======"+jsonObject.get("printProperties").getAsJsonObject().get("servercertkey").getAsString());
			//Map<String, Object> validMap = machineService.validateRequest(prinCerKey, printJobId, printParam, machineId) ;
			//if ( validMap != null && (boolean)validMap.get("valid")) {
				System.out.println(" This is reciever 2 ");
				
				printDocRequest.setMachineId(machineId);
				printDocRequest.setPrintJobId(printJobId);
				printDocRequest.setEmailId(emailId);
				printDocRequest.setRequestType(RequestType.DIRECT.name());
				//printDocRequest.setPrintSecurityKey(prinprop.getServercertkey());
			//	printDocRequest.setPrintCost((float)validMap.get("cost"));
				//printDocRequest.setPageCount((int)validMap.get("pageCount"));
				ObjectMapper mapper = FileWriteUtility.getObjectMapper();
				printDocRequest.setPrintProperties(prinprop);
				resultMap = machineService.filereceiver(file, printDocRequest);
				
				
			//}else {
				//return new ResponseEntity<String>("INVALID", HttpStatus.BAD_REQUEST);		
			//}

		} catch (Exception e) {
			
			logger.error("  Failed to Process  ", e);
			resultMap.put(MachineConstant.errorCode, ErrorCode.M102.name());
			resultMap.put(MachineConstant.errorMSG, ErrorCode.M102.getErrorMSG());
			PrintJobStatusUpdate printJobStatusUpdate = CommonUtility.createUplodatePayload(printDocRequest, 
					ErrorCode.M102.getErrorMSG(), ErrorCode.M102.name(), PrintStatus.FAIL.name());
			requestStatusUpdateService.updateRequestStatusOnCloud(printJobStatusUpdate);
		}
		return new ResponseEntity<Map<String, String>>(resultMap, HttpStatus.OK);
		
	}
	
	// Check this API machine is ready to accept request or Not
	// Check HashMap Sender is very frequently sending request to crash system
	// Check Printer is ready to Process request
	@GetMapping(value = "/ready")
	public ResponseEntity<GenericResponseEntity<Boolean> > checkReady(
			@RequestParam(name="emailId", required=false) String emailId){
		
		System.out.println("This is Readiness check ============");
		boolean ready = machineService.checkReadiness(emailId);
		GenericResponseEntity<Boolean> result = new GenericResponseEntity<Boolean>(ready);
		return new ResponseEntity<GenericResponseEntity<Boolean>>(result, HttpStatus.OK);
		
	}
	
}
