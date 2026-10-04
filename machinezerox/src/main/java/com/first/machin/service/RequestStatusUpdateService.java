package com.first.machin.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.machin.persistence.PersistRequestLocalService;
import com.first.machin.persistence.SQLLiteConnection;

import om.first.machin.model.PrintDocRequest;
import om.first.machin.model.PrintJobStatusUpdate;

@Service 
public class RequestStatusUpdateService {
	private Logger logger = LoggerFactory.getLogger(RequestStatusUpdateService.class);
	
	@Value("${zerox.cloud.updateStatusURL}")
	private String updateStatusURL;	
	
	
	
	@Value("${zerox.cloud.IPupdateURL}")
	private String ipUpdateURL;	
	
	@Value("${zerox.cloud.getPrintJobDetails}")
	private String printJobDetailsURL;	
	
	@Autowired
	SQLLiteConnection sQLLiteConnection;
		
	private RestTemplate restTemplate;
	
	@Autowired
	PersistRequestLocalService persistRequestLocalService;
	
	
	
	public String updateRequestStatusOnCloud(PrintJobStatusUpdate printJobStatusUpdate) {
		logger.info("Update Print Job Status on Cloud" + updateStatusURL);
		String status = null;
		ObjectMapper objectMapper = new ObjectMapper();
		String postJson;
		restTemplate = new RestTemplate();
		try {
			
		//	persistRequestLocalService.persistStatusInLocalDB(printJobStatusUpdate);
			postJson = objectMapper.writeValueAsString(printJobStatusUpdate);
		
		// invoke center APIs
		HttpHeaders headers = new HttpHeaders();
		// headers.set("sessionId", sessionid);
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
		
		HttpEntity<String> entity = new HttpEntity<String>(postJson, headers);
	
		
		System.out.println(" ============Cloud Update :========== "+ updateStatusURL +"\n "+ entity.toString());
		ResponseEntity<String> response;
		try {
			response = restTemplate.exchange(new URI(updateStatusURL), HttpMethod.POST,
					entity, String.class);
			if(response !=null && response.getStatusCodeValue() != 200) {
				logger.error(" Failed to update");
			}
			logger.info(""+response.toString());
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// persist local for noificaion
			persistRequestLocalService.updateLocalCloudNotifStatus(printJobStatusUpdate, false);
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// persist localpersistRequestLocalService
			persistRequestLocalService.updateLocalCloudNotifStatus(printJobStatusUpdate, false); 
		}
		
		
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error(" Update Status Failed on Cloud :", e);
		}
		return status;
		
	}
	
	public void updateIPStatusOnCloud(String machineId, String hotspotIP) {
		logger.info("Update Print Job Status on Cloud" + updateStatusURL);
		restTemplate = new RestTemplate();
		try {
			
		HttpHeaders headers = new HttpHeaders();
		// headers.set("sessionId", sessionid);
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
		ResponseEntity<String> response;
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		StringBuilder url = new StringBuilder(ipUpdateURL);
		url.append("?machineId=").append(machineId).append("&ipaddress=").append(hotspotIP);
		System.out.println(" ==============================================="+url.toString());
		try {
			response = restTemplate.exchange(new URI(url.toString()), HttpMethod.GET,
					entity, String.class);
			
			System.out.println(" Response ="+ response);
			if(response !=null && response.getStatusCodeValue() != 200) {
				logger.error(" Failed to update");
			}
		
			logger.info(""+response.toString());
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(" Update Status Failed on Cloud :", e);
		}
	
	}

	public PrintDocRequest getPrintJobDetailsValidate(PrintDocRequest printDocRequest) {
		logger.info(" Fetch PrintJOB  details cloud ============");
		ObjectMapper objectMapper = new ObjectMapper();
		restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		// headers.set("sessionId", sessionid);
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
		
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		
		System.out.println(" ============Cloud Update :========== "+ printJobDetailsURL +"\n "+ entity.toString());
		ResponseEntity<PrintDocRequest> response = null;
		try {
			String url = printJobDetailsURL+printDocRequest.getPrintJobId();
			System.out.println(" *************Cloud  URL ********"+url);
			response = restTemplate.exchange(new URI(url), HttpMethod.GET,
					entity, PrintDocRequest.class);
			System.out.println(" Get Response "+ response);
			if(response !=null && response.getStatusCodeValue() != 200) {
				logger.error(" Failed to update");
				return null;
			}
			logger.info(""+response.toString());
			
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(" failed to call ", e);
			
		} catch (URISyntaxException e) {
			logger.error(" failed to call ", e);
	
		}
		
	return response.getBody();
	
	}
	
	
}
