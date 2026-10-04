package com.first.test.controller;

import java.util.List;

import org.aspectj.apache.bcel.classfile.Module.Require;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.first.test.model.PaymentDetails;
import com.first.test.model.PaymentDetailsForMongoDB;
import com.first.test.model.PrintDocRequest;
import com.first.test.model.WalletModel;
//import com.first.test.payment.PaymentDetails;
import com.first.test.payment.PaytmPaymentService;
import com.first.test.service.CostDerivativeService;
import com.first.test.service.PaymentService;
import com.first.test.util.GenericResponseEntity;
import com.first.test.util.ServiceStatus;
@RestController
@RequestMapping("/first/payment")
public class PaymentOrderController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentOrderController.class);
	@Autowired
	PaytmPaymentService paytmPaymentService;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	CostDerivativeService costDerivativeService;
	

	// Save the Payment Details executed by Gateway
	@PostMapping("/paytm/initiatepayment")
	ResponseEntity<JsonNode> savePaymentDetails(@RequestBody JsonNode PaymentDetails){
		
		logger.info("======== Reciev Payment "+ PaymentDetails.toString());
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = paytmPaymentService.paymentInitiate(PaymentDetails);
		ObjectNode objectNode = objectMapper.createObjectNode();
		
		return new ResponseEntity<JsonNode>(jsonNode, HttpStatus.OK);
	}
	
	
	@PostMapping("/paytm/getchecksum")
	String getchecksum(@RequestBody JsonNode paymentDetails){
		
		System.out.println("======== Reciev Payment "+ paymentDetails.toString());
		ObjectMapper objectMapper = new ObjectMapper();
		return paytmPaymentService.getCheckSum(paymentDetails);
	
	}
	
	@PostMapping("/paytm/verifychecksum")
	boolean verifychecksum(@RequestBody JsonNode paymentDetails){
		
		System.out.println("======== Reciev Payment "+ paymentDetails.toString());
		ObjectMapper objectMapper = new ObjectMapper();
		return paytmPaymentService.checksumVerification(paymentDetails);
	
	}
	
	
	// callback
	
	//paytm.callbackUrl
	@GetMapping("/paytm/paytmcallback")
	ResponseEntity<JsonNode> savePaymentDetails(){
		
		System.out.println("========Call Back Reciev Payment ");
		
		return new ResponseEntity<JsonNode>( HttpStatus.OK);
	}
	
	@GetMapping("/orderPayhistory")
	ResponseEntity<GenericResponseEntity<List<PaymentDetailsForMongoDB>>> orderPayhistory( @RequestParam("emailId") String emailId){
		
		logger.info("========Call Back Reciev Payment ");
		List<PaymentDetailsForMongoDB> pritOrderList = paymentService.orderHistory(emailId );
		System.out.println("==========  "+ pritOrderList.toString());
		GenericResponseEntity<List<PaymentDetailsForMongoDB>> result = new GenericResponseEntity<List<PaymentDetailsForMongoDB>>(pritOrderList, ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<List<PaymentDetailsForMongoDB>>>( result, HttpStatus.OK);
	}
	
	@PostMapping("/payment/acceptPayment")
	ResponseEntity<GenericResponseEntity<String>> acceptPayment(
			@RequestBody PaymentDetailsForMongoDB paymentDetails) {
		 GenericResponseEntity<String> result = null;
		boolean status = paymentService.acceptPayment(paymentDetails);
		if(status) {
		   result = new GenericResponseEntity<String>(ServiceStatus.SUCCESS.name(), ServiceStatus.SUCCESS.name(), HttpStatus.OK.value());
		}else {
			  result = new GenericResponseEntity<String>(ServiceStatus.FAILED.name(), ServiceStatus.FAILED.name(), HttpStatus.BAD_REQUEST.value());
		}
		return new ResponseEntity<GenericResponseEntity<String>>(result, HttpStatus.OK);
	}

	/*
	 * Order History by EmailId 
	 */
	

	@GetMapping("/payment/orderhistory")
	ResponseEntity<List<PaymentDetails> > orderHistoryPayment( @RequestParam("emailId") String emailId){
		
		logger.info("========Call Back Reciev Payment ");
		List<PaymentDetails> pritOrderList = paymentService.orderHistoryPayment(emailId );
		return new ResponseEntity<List<PaymentDetails>>( pritOrderList, HttpStatus.OK);
	}
	
	// check balance
	@GetMapping("/payment/checkbalance")
	ResponseEntity<GenericResponseEntity<WalletModel>> checkbalance(
			@RequestHeader(name= "Authorization", required=true) String authorization, 
			@RequestParam("emailId") String emailId,
			@RequestParam("walletId") String walletId){
		
		logger.info("========Call Back Reciev Payment ");
		WalletModel wallet = paymentService.checkbalance(emailId, walletId );
		GenericResponseEntity<WalletModel> result = new GenericResponseEntity<WalletModel>(wallet, ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<WalletModel>>( result, HttpStatus.OK);
	}
	
	
	// Save the Order Details
	
	// Fetch the Order History by UserID
	
	// Get Refund Status 
	
	// Update the Wallet Details 
	
	// Add Money in Wallet
	
	// Find cost of PrintOuts 
	
}
