package com.first.test.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.first.test.model.BulkPrintOrder;
import com.first.test.service.AdminService;
import com.first.test.util.CommonUtility;
import com.first.test.util.GenericResponseEntity;
import com.google.gson.Gson;

@RestController
@RequestMapping(value = "/first/bulk")
public class BulkPrintController {

	private static Logger logger = LoggerFactory.getLogger(BulkPrintController.class);
	
	@Autowired
	Gson gson;
	
	@Autowired
	AdminService adminservce;

	// Get Bulk Order
	@PostMapping("/bulkOrder")
	ResponseEntity<GenericResponseEntity<BulkPrintOrder> > bulkOrder(@RequestParam("file") MultipartFile file,
			@RequestParam("coverfile") MultipartFile coverfile, @RequestParam(name = "data") String data) {

		BulkPrintOrder bulkOrder = gson.fromJson(data, BulkPrintOrder.class);
		logger.info("======= Bulk Print =============== {}", bulkOrder);
		bulkOrder = adminservce.buldOrder(file, coverfile, bulkOrder);
		GenericResponseEntity<BulkPrintOrder> result = new GenericResponseEntity<BulkPrintOrder>(bulkOrder);
		return new ResponseEntity<GenericResponseEntity<BulkPrintOrder> >(result, HttpStatus.OK);
	}

	@GetMapping("/uidetails")
	ResponseEntity< GenericResponseEntity<Map<String, Object>>> getUIDetails() {
        GenericResponseEntity<Map<String, Object>> uiMap = new GenericResponseEntity( CommonUtility.getUIMap());
		return new ResponseEntity< GenericResponseEntity<Map<String, Object>>>(uiMap, HttpStatus.OK);
	}
	
	@GetMapping("/deliverable")
	ResponseEntity< GenericResponseEntity<Map<String, String> >> pincode(@RequestParam(name="pincode") String pincode) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pincode", pincode);
		map.put("delivery", "YES");
        GenericResponseEntity<Map<String, String>> result = new GenericResponseEntity("yes");
		return new ResponseEntity< GenericResponseEntity<Map<String, String>>>(result, HttpStatus.OK);
	}
	
	
	@PostMapping("/placeOrder")
	ResponseEntity<GenericResponseEntity<BulkPrintOrder> > placeOrder( @RequestParam(name = "data") String data) {

		BulkPrintOrder bulkOrder = gson.fromJson(data, BulkPrintOrder.class);
		logger.info("======= Bulk Print =============== {}", bulkOrder);
		bulkOrder = adminservce.placeOrder(bulkOrder);
		GenericResponseEntity<BulkPrintOrder> result = new GenericResponseEntity<BulkPrintOrder>(bulkOrder);
		return new ResponseEntity<GenericResponseEntity<BulkPrintOrder> >(result, HttpStatus.OK);
	}
	
	@GetMapping("/listOfPendingOrders")
	ResponseEntity<GenericResponseEntity<List<BulkPrintOrder>> > listOfPendingOrders( @RequestParam(name = "emailId") String emailId) {

		logger.info("======= Bulk Print =============== {}", emailId);
		List<BulkPrintOrder> bulkOrders = adminservce.listOfPendingOrders(emailId);
		GenericResponseEntity<List<BulkPrintOrder>> result = new GenericResponseEntity<List<BulkPrintOrder>>(bulkOrders);
		return new ResponseEntity<GenericResponseEntity<List<BulkPrintOrder>> >(result, HttpStatus.OK);
	}
	
	@GetMapping("/orderDetailsByOrderId")
	ResponseEntity<GenericResponseEntity<BulkPrintOrder> > orderDetailsByOrderId( @RequestParam(name = "orderId") String orderId) {
		 try {
			orderId = URLDecoder.decode(orderId, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("======= Bulk Print =============== {}", orderId);
		BulkPrintOrder bulkOrder = adminservce.orderDetailsByOrderId(orderId);
		GenericResponseEntity<BulkPrintOrder> result = new GenericResponseEntity<BulkPrintOrder>(bulkOrder);
		return new ResponseEntity<GenericResponseEntity<BulkPrintOrder> >(result, HttpStatus.OK);
	}
	
}
