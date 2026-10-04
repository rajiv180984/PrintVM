package com.first.test.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.first.test.config.ServiceConfig;
import com.first.test.model.Feedback;
import com.first.test.util.AESUtility;

@RestController
@RequestMapping(value = "/first/auth")
public class AuthenticationController {

	private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	
	@Autowired
	ServiceConfig serviceConfig;
	
	
	//PostToken
	
	@PostMapping(value = "/secure/tkn")
	public HttpEntity<String> getAutToken(@RequestBody AuthenticateModel authenticateModel) {
		StringBuilder authUrl = new StringBuilder(serviceConfig.getAuthUrl()); 
		String password = AESUtility.decrypt(authenticateModel.getUserpwd());
		String username = AESUtility.decrypt(authenticateModel.getUsername());
		logger.info("URL: "+authUrl +"\n User Name:"+ authenticateModel.getUsername() + "\n password "+ password);

		HttpHeaders headers = new HttpHeaders();
	//	headers.set("Content-Type", "application/json");
		//headers.set("Accept", "application/json");
		//application/x-www-form-urlencoded
		headers.set("Authorization", "Basic Y2xpZW50SWQ6c2VjcmV0");

		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		params.put("password", password);
		params.put("grant_type", "password");		
		/*params.put("clientVersion", clientVersion);
		params.put("clientType", clientType);
		params.put("issuerName", issuerName);
		params.put("applicationName", applicationName);
*/
		HttpEntity entity = new HttpEntity(headers);
		RestTemplate restTemplate = new RestTemplate();
	//	restTemplate.execu
		//http://13.232.52.77:9000/oauth/token?grant_type=password&username=
		StringBuilder authAPIURL = authUrl.append(username).append("&password=").append(password);
		logger.info("==== Oauth URL ====="+authAPIURL);
		
		HttpEntity<String> response = restTemplate.exchange(authAPIURL.toString(), HttpMethod.POST, entity, String.class, params);
	
		
		if(response !=null) {
			logger.info("======== "+ response );
		}
		return response;
	}
	
	//GetToken
	@GetMapping(value="testAPI")
	public HttpEntity<Feedback>  getTestingAPI(){
		Feedback fd = new Feedback();
		fd.setComment("Testing");
		fd.setEmailId("Test@gmail.com");
		fd.setReplyMsg("This is replys");
		return  new ResponseEntity<Feedback>(fd, HttpStatus.OK);
	}
	
	
}
