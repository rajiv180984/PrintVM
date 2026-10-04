package com.first.test.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.json.JSONObject;
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
import org.springframework.web.multipart.MultipartFile;

import com.first.test.model.CostDerivative;
import com.first.test.model.Factor2OTPResponse;
import com.first.test.model.OTPRecord;
import com.first.test.model.PrintDocRequest;
import com.first.test.model.PrintJobStatusUpdate;
import com.first.test.model.PublicDocDetails;
import com.first.test.model.ShareDocForMachineByProfessionals;
import com.first.test.model.ThirdPartyPaymentDetails;
import com.first.test.model.UserCouponOffer;
import com.first.test.model.WIFIMachine;
import com.first.test.model.ZeroxPaymentDetails;
import com.first.test.service.AdminService;
import com.first.test.service.CostDerivativeService;
import com.first.test.service.OfferService;
import com.first.test.service.PaymentService;
import com.first.test.service.UserPrintDocService;
import com.first.test.util.GenericResponseEntity;
import com.first.test.util.ServiceStatus;
import com.google.gson.Gson;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping(value = "/first/admin")
public class AdminController {

	private static Logger logger = LoggerFactory.getLogger(AdminController.class);

	// add Public doc availble for all machine
	@Autowired
	Gson gson;

	@Autowired
	AdminService adminservce;

	@Autowired
	OfferService offerService;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	CostDerivativeService costDerivativeService;
	
	@Autowired
	UserPrintDocService userPrintDocService;
	
	@PostMapping(value = "/publicdoc")
	public ResponseEntity<PublicDocDetails> addPublicDocument(@RequestParam("file") MultipartFile file,
			@RequestParam(name = "docDetailsJson") String docDetailsJson) {
		
		PublicDocDetails publicDocDetails = gson.fromJson(docDetailsJson, PublicDocDetails.class);
		String s3bucketPath = adminservce.addPublicDocument(file, publicDocDetails);
		publicDocDetails.setS3path(s3bucketPath);
		return new ResponseEntity<PublicDocDetails>(publicDocDetails, HttpStatus.OK);
	}

	// what ever JSON Object is return from previous publicdoc api must used for to
	// send doc to perticular machines
	// or on scan qr code/ selecting the location. In public doc Tab, it is showing
	// Public doc
	@PostMapping(value = "/publicdoctomachines")
	public ResponseEntity<PublicDocDetails> publicdoctomachines(@RequestParam("file") MultipartFile file,
			@RequestParam(name = "docDetailsJson") String docDetailsJson) {
		
		PublicDocDetails publicDocDetails = null;
		try {
			docDetailsJson = URLDecoder.decode(docDetailsJson, StandardCharsets.UTF_8.name());
			 publicDocDetails = gson.fromJson(docDetailsJson, PublicDocDetails.class);
			 publicDocDetails = adminservce.publicdoctomachines(file, publicDocDetails);
			
			return new ResponseEntity<PublicDocDetails>(publicDocDetails, HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<PublicDocDetails>(publicDocDetails, HttpStatus.BAD_REQUEST);
	}

	//Add Counpon for Users
	@PostMapping(value="UserCoupon")
	public ResponseEntity<String> assignCouponToUser(@RequestBody List<UserCouponOffer> listCopupon){
		offerService.addCopoun(listCopupon);
		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
	}
	
	// Payment update in System from Android
	// Mock API
	@PostMapping(value="/paymentMockAPI")
	public ResponseEntity<String> paymentMock( @RequestBody ThirdPartyPaymentDetails thirdPartyPaymentDetails){
		
		String response = paymentService.mockPaymentAPI(thirdPartyPaymentDetails);
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
	
	// Add machine cost derivatives
	@PostMapping(value="/addMachineCostDerivative")
	public ResponseEntity<String> addMachineCostDerivative(@RequestBody CostDerivative CostDerivative){
		costDerivativeService.addCostDerivatives(CostDerivative);
		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
	}
	
	/*
	 * Get All Category
	 */
	@GetMapping(value="/getAllCategory")
	public ResponseEntity<List<String>> getAllCategory(){
		
		List<String> list= adminservce.getAllCategory();
		return  new ResponseEntity<List<String>>(list, HttpStatus.OK);
	}
	
	
	@GetMapping(value="/getAllCategoryAtMachine")
	public ResponseEntity<List<String>> getAllCategoryByMachineId(@RequestParam(name="machineId", required=true )String machineId){
		
		List<String> list= adminservce.getAllCategoryByMachineId(machineId);
		return  new ResponseEntity<List<String>>(list, HttpStatus.OK);
	}

	
	// Dangerous API 
	// Add Test Balance
	@PostMapping("/testBalance")
	public ResponseEntity<ZeroxPaymentDetails> testAddBalance(@RequestBody ZeroxPaymentDetails zeroxPaymentDetails){
		
		zeroxPaymentDetails.setEmailId("testUser@mailinator.com");
		zeroxPaymentDetails.setMobileNo("9876543211");
		zeroxPaymentDetails.setCustId("123123");
		zeroxPaymentDetails.setZeroxBalance(100.50f);
		zeroxPaymentDetails = adminservce.testAddWalletBalance(zeroxPaymentDetails);
		return new ResponseEntity<ZeroxPaymentDetails>(zeroxPaymentDetails, HttpStatus.OK);
	}
	
	@GetMapping("/getDocListByCategory")
	public ResponseEntity<GenericResponseEntity<List<ShareDocForMachineByProfessionals>> >  getDocListByCategory(
			@RequestParam("category") String category,
			@RequestParam(name = "machineId", required=false) String machineId){
		List<ShareDocForMachineByProfessionals> docList = adminservce.getDocListByCategory(category, machineId);
		GenericResponseEntity<List<ShareDocForMachineByProfessionals>> docRes = new GenericResponseEntity<List<ShareDocForMachineByProfessionals>>(docList, ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<List<ShareDocForMachineByProfessionals>> >(docRes, HttpStatus.OK );
	}
	
	@GetMapping("/getDocListByCategoryMachineId")
	public ResponseEntity<GenericResponseEntity<List<ShareDocForMachineByProfessionals>> >  getDocListByCategoryAndMachineId(
			@RequestParam("category") String category,
			@RequestParam(name = "machineId", required=true) String machineId){
		List<ShareDocForMachineByProfessionals> docList = adminservce.getDocListByCategoryAndMachineId(category, machineId);
		GenericResponseEntity<List<ShareDocForMachineByProfessionals>> docRes = new GenericResponseEntity<List<ShareDocForMachineByProfessionals>>(docList, ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<List<ShareDocForMachineByProfessionals>> >(docRes, HttpStatus.OK );
	}
	
	
	@PostMapping("/getWIFIMachineDetails")
	public ResponseEntity<GenericResponseEntity<WIFIMachine>> getWIFIMachineDetails(
			@RequestHeader(name="Authorization", required=true) String Authorization,
			@RequestHeader(name="emailId", required=true) String emailId,
			@RequestParam(name="printjobId",required=false) String printjobId,
			@RequestBody WIFIMachine wifiMachine ){
		
		System.out.println(" ============= mAchine :"+ wifiMachine.toString());
		WIFIMachine data = adminservce.getWIFIMachineDetails(wifiMachine, printjobId, emailId);
		GenericResponseEntity<WIFIMachine> result = new GenericResponseEntity<WIFIMachine>(data);
		return new ResponseEntity<GenericResponseEntity<WIFIMachine>> (result, HttpStatus.OK);
	}
	
	@PostMapping("/getrazorpayorder")
	public  ResponseEntity<Map<String, String>> getRazorpay(
			@RequestHeader(name="Authorization", required=true) String Authorization,
			@RequestHeader(name="emailId", required=true) String emailId,	
			@RequestBody Map<String, String> razorPay ){
		
		try {
			// FIXME Put in config file
			RazorpayClient razorpayClient = new RazorpayClient("rzp_test_Y4wnGJukuLXtvX", "bpJ2bBJZmY2yf6wGKUyxMS0G");
			
			JSONObject options = new JSONObject();
			UUID uuid = UUID.randomUUID();
			System.out.println(" This is  "+ razorPay.toString() + "=========="+razorPay.get("amount") + " This is :"+ Float.parseFloat(razorPay.get("amount"))) ;
			
			// Note: if want o recharge with 10 ruppeas than pass 10*100 Rs
			options.put("amount", Float.parseFloat(razorPay.get("amount"))*100);
			options.put("currency", "INR");
			options.put("receipt", uuid.toString());
			Order order = razorpayClient.Orders.create(options);
			
			System.out.println(" ======================== "+ order.toString());
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("orderId", order.get("id"));
			map.put("receipt", order.get("receipt"));
			
			return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
		} catch (RazorpayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Map<String, String>>(HttpStatus.BAD_REQUEST);
		     
	}
	
	// OTP send for activation
	@GetMapping("/activationOTP")
	public ResponseEntity<GenericResponseEntity<Factor2OTPResponse>> activationOTP(
			@RequestParam(name = "mobileNo", required = true) String mobileNo) {
		// check number already Exist
		// last time send OTP number otherwise Someone can exhashust OTP
		Factor2OTPResponse otpResponse = adminservce.generateOTP(mobileNo);
		GenericResponseEntity<Factor2OTPResponse> result = new GenericResponseEntity<Factor2OTPResponse>(otpResponse);
		return new ResponseEntity<GenericResponseEntity<Factor2OTPResponse>>(result, HttpStatus.OK);
	}
	
	@GetMapping("/updateMachineIP")
	public ResponseEntity<String> updateMachineIP(
			@RequestParam(name = "machineId", required = true) String machineId,
			@RequestParam(name = "ipaddress", required = true) String ipaddress) {
		// check number already Exist
		// last time send OTP number otherwise Someone can exhashust OTP
		adminservce.updateMachineIP(machineId, ipaddress);
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
	
	@GetMapping("/getPrintJobDetails")
	public ResponseEntity<PrintDocRequest> getPrintJobDetails(
			@RequestParam(name = "machineId", required = true) String machineId,
			@RequestParam(name = "printJobId", required = true) String printJobId) {
		// check number already Exist
		// last time send OTP number otherwise Someone can exhashust OTP
		PrintDocRequest printDocRequest = userPrintDocService.getPrintJobDetails(machineId, printJobId);
		return new ResponseEntity<PrintDocRequest>(printDocRequest, HttpStatus.OK);
	}
	// update print status in db request come from Machine on success or fail and paper, ink for refill
	@PostMapping(value = "/updatePrintStatus")
	public ResponseEntity<String> updatePrintJOBFromMachine(@RequestBody PrintJobStatusUpdate PrintJobStatusUpdate) {
		userPrintDocService.updatePrintJobstatus(PrintJobStatusUpdate);
		return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
	}

}
