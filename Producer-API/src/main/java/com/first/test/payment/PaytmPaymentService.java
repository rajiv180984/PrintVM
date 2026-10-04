package com.first.test.payment;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JsonNode;

public interface PaytmPaymentService {
	
	JsonNode paymentInitiate(JsonNode paymentDetails);
	JsonNode getPaymentStatus();
	String getCheckSum(JsonNode paymentDetails);
	
	boolean checksumVerification(JsonNode paymentDetails);
	
	
	
	

}
