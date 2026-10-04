package com.first.test.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.first.test.model.PartnerDetails;
import com.first.test.model.PartnerItemRequest;
import com.first.test.service.PartnerService;

@RestController
@RequestMapping(value = "/first/partner")
public class PartnerController {

	@Autowired
	PartnerService partnerService;
	// get list of all item belongs to partner by partnerId
	@PostMapping(value="/allrequest")
	public ResponseEntity<Object> getallRequestItem(@RequestBody PartnerItemRequest partnerItemRequest){
		partnerService.getListOfAllItemByPartnerId(partnerItemRequest.getPartnerId(), partnerItemRequest.getMachineId());
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	// publish item to device 
	
	@PostMapping(value="publishCommand")
	public ResponseEntity<String> publishCommand(@RequestBody PartnerItemRequest partnerItemRequest) {
		partnerService.publishPrintCommand(partnerItemRequest);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@PostMapping(value="registerPartner")
	public ResponseEntity<PartnerDetails> registerPartner(@RequestBody PartnerDetails partnerDetails) {
		 partnerDetails =  partnerService.registerPartner(partnerDetails);		
		return new ResponseEntity<PartnerDetails>(partnerDetails, HttpStatus.OK);
	}
	

}
