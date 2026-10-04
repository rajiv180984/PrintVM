package com.first.machin.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import om.first.machin.model.PrintDocRequest;

public interface MachineService {
	boolean checkReadiness(String emailId);

	Map<String, Object>  validateRequest(String servercertkey, String printJobId, String printParam,  String machineId);

	Map<String, String> filereceiver(MultipartFile file, PrintDocRequest printDocRequest);

	
	

}
