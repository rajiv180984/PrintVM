package com.first.hivemqtt;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class Demo {
 public static void main(String[] args) {
	
	 String url = "http://localhost:8185/zerox/first/dream/updatePrintStatus";
	 HttpHeaders headers = new HttpHeaders();
		// headers.set("sessionId", sessionid);
		headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(org.springframework.http.MediaType.APPLICATION_JSON));
		
		HttpEntity<String> entity = new HttpEntity<String>("{}", headers);
			 RestTemplate rest = new RestTemplate();
			 try {
				ResponseEntity< String> response = rest.exchange(new URI(url), HttpMethod.POST, entity, String.class);
				System.out.println(" This is response :"+response);
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
}
}
