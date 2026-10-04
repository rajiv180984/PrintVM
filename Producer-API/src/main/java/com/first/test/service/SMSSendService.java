package com.first.test.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;

//import org.apache.commons.lang.text.StrSubstitutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.first.test.config.ServiceConfig;
import com.first.test.exception.OTPAuthenticationException;
import com.first.test.model.Factor2OTPResponse;
import com.first.test.util.ErrorCodeNo;
import com.first.test.util.ZeroxConstants;
import com.google.gson.Gson;

@Service
public class SMSSendService {

	private Logger logger = LoggerFactory.getLogger(SMSSendService.class);
	
	@Autowired
	private Gson gson;
	
	@Autowired
	ServiceConfig serviceConfig;

	
	public Factor2OTPResponse sendOTPForActivateAccount(String mobileNo) {
		// https://2factor.in/API/V1/0188b88c-0a2d-11ea-9fa5-0200cd936042/SMS/8904668406/AUTOGEN
		Factor2OTPResponse factor2OTPResponse = null;
		
		try {
		
		StringBuilder urlBuilder = new StringBuilder(serviceConfig.getFactor2baseURL());
		urlBuilder.append(serviceConfig.getFactorAPIKEY()).append("/").append("SMS/").append(mobileNo).append("/")
				.append("AUTOGEN");

		logger.info("OTP Number :" + urlBuilder.toString());
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, null,
				String.class);
		logger.info("12================= " + response.getBody());
		if (response.getStatusCodeValue() != 200) {
			factor2OTPResponse = gson.fromJson(response.getBody(), Factor2OTPResponse.class);
			if(factor2OTPResponse.getStatus().equalsIgnoreCase("Error") && factor2OTPResponse.getDetails().contains("Invalid Phone Number")) {
				factor2OTPResponse = new Factor2OTPResponse();
				factor2OTPResponse.setStatus(String.valueOf(ErrorCodeNo.INVALID_MOBILE.getValue()));
				factor2OTPResponse.setDetails(ErrorCodeNo.INVALID_MOBILE.name()+" OR Service not available to Process");
				return factor2OTPResponse;
			}
		}
		factor2OTPResponse = gson.fromJson(response.getBody(), Factor2OTPResponse.class);
		logger.info(" Send OT number :" + response.toString());
		
		}catch (Exception e) {
			logger.error("Failed to send OTP API call or Parse {}",e.getMessage(), e);
			factor2OTPResponse = new Factor2OTPResponse();
			factor2OTPResponse.setStatus(String.valueOf(ErrorCodeNo.INVALID_MOBILE.getValue()));
			factor2OTPResponse.setDetails(ErrorCodeNo.INVALID_MOBILE.name()+" OR Service not available to Process");
			return factor2OTPResponse;
		
		}
		return factor2OTPResponse;

	}
	
	
	public Factor2OTPResponse sendSMS(String mobileNo, String customerName, String newPassword) {
		// https://2factor.in/API/V1/0188b88c-0a2d-11ea-9fa5-0200cd936042/SMS/8904668406/AUTOGEN
		Factor2OTPResponse factor2OTPResponse = null;
		
		
		//http://2factor.in/API/V1/293832-67745-11e5-88de-5600000c6b13/ADDON_SERVICES/SEND/TSMS
		StringBuilder urlBuilder = new StringBuilder(serviceConfig.getFactor2baseURL());
		urlBuilder.append(serviceConfig.getFactorAPIKEY()).append("/ADDON_SERVICES/SEND/TSMS");
	
		logger.info("OTP Number :" + urlBuilder.toString());
		RestTemplate restTemplate = new RestTemplate();
		
		String resetPasswordBody = ZeroxConstants.passwordResetSMS;
		Map<String, String> smsBody = new HashMap<>();
		smsBody.put("name", customerName);
		smsBody.put("password", newPassword);
		String formattedString = StrSubstitutor.replace(resetPasswordBody, smsBody);
		
		Map<String, String > smsmap = new HashMap<>();
		
		smsmap.put("From", "TFCTOR");
		smsmap.put("To", mobileNo);
		smsmap.put("Msg", formattedString);
		
	     
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE);    
	    
	    HttpEntity<String> request = new HttpEntity<>(gson.toJson(smsmap), headers);
		ResponseEntity<String> response = restTemplate.exchange(urlBuilder.toString(), HttpMethod.POST, request,
				String.class);
		logger.info("12================= " + response.getBody());
		if (response.getStatusCodeValue() != 200) {
			logger.error("OTP Message Failure: " + response.getStatusCodeValue());
			throw new OTPAuthenticationException("Authentication Service Unavailable");
		}
		try {
			factor2OTPResponse = gson.fromJson(response.getBody(), Factor2OTPResponse.class);
		} catch (Exception e) {
			logger.error("Failed to Parse ", e);
		}
		logger.info(" Send OT number :" + response.toString());
		return factor2OTPResponse;

	}
	
}
