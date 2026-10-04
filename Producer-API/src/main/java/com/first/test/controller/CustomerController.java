package com.first.test.controller;

import javax.websocket.server.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.first.test.model.ContrbutorMachines;
import com.first.test.model.Customer;
import com.first.test.model.Factor2OTPResponse;
import com.first.test.model.ResetProfile;
import com.first.test.model.UserCredentials;
import com.first.test.model.WalletModel;
import com.first.test.model.ZeroxPaymentDetails;
import com.first.test.service.CustomerProfileService;
import com.first.test.service.PaymentService;
import com.first.test.util.AESUtility;
import com.first.test.util.GenericResponseEntity;
import com.first.test.util.ProfileStatus;
import com.first.test.util.ServiceStatus;

@RestController
@RequestMapping(value = "/first/customer")
public class CustomerController {

	private static final String USERNAME = "username";

	private static final String GRANT_TYPE = "grant_type";
	
	private static final String PASSWORD = "password";

	
	
	private static Logger logger = LoggerFactory.getLogger(SmartOfficeServiceController.class);

	@Autowired
	CustomerProfileService customerService;
	
	@Autowired
	PaymentService paymentService;
	
	
	//create Customer Profile
	@PostMapping(value="/profile")
	public ResponseEntity<GenericResponseEntity<Factor2OTPResponse>> createProfile(
			@RequestBody Customer customer,
			/*
			 * @RequestParam(value="custName", required = true) String custName,
			 * 
			 * @RequestParam(value="emailId", required = true) String emailId,
			 * 
			 * @RequestParam(value="mobileNo", required = true) String mobileNo,
			 * 
			 * @RequestParam(value="password", required = true) String password,
			 * 
			 * @RequestParam(value="cityName", required = true) String cityName,
			 */
			@RequestParam(value="promocode", required = false) String promocode
			){
		
		logger.info("==== Register Request Body ========:"+ customer.toString());
		// Customer customer = new Customer();
		 //customer.setCustName(custName);
		 customer.setEmailId(customer.getEmailId().toLowerCase());
		// customer.setMobileNo(mobileNo);
		// customer.setCityName(cityName);
		 customer.setPassword(AESUtility.decrypt(customer.getPassword()));
		 customer.setUserCredentials(new UserCredentials());
		 customer.setActiveStatus(ProfileStatus.NEW.name());
		// customer.setPromocode(promocode);
		 
		Factor2OTPResponse customerResult = customerService.createCustomerProfile(customer);
		// send OTP number
		logger.info("=============="+customerResult.toString());
		GenericResponseEntity<Factor2OTPResponse> data = new GenericResponseEntity<Factor2OTPResponse>(customerResult,"SUCCESS", 200);
		
		return new ResponseEntity<GenericResponseEntity<Factor2OTPResponse>>(data, HttpStatus.OK);
	}
	
	
	// Login  grant_type=password&username=rathor.rajeev@gmail.com&password=HZ6hsVh@
	@PostMapping("/oauth/token")
	public String login(@RequestParam(value=GRANT_TYPE, required=true) String  grant_type,
			@RequestParam(value=USERNAME, required=true) String  username,
			@RequestParam(value=PASSWORD, required=true) String  password,
			@RequestParam(value="app_id", required=false) String  app_id,
			@RequestParam(value="key", required=false) String  key) {
		
		Map<String, String> map = new HashMap<String, String>();
		map.put(GRANT_TYPE, grant_type);
		map.put(USERNAME, username);
		map.put(PASSWORD, username);
		customerService.getLoginDetails(map);
		
		return "";
	}
	
	
	public String logout(@RequestParam(value=USERNAME, required=true) String  username) {
		customerService.logout( username);
		return "";
	}
	
	@GetMapping(value="/forgetPassword")
	public ResponseEntity<GenericResponseEntity<String>> forgetProfile(@RequestParam("userDetail") String userDetail){
		logger.info("========================= "+ userDetail);
		String result  = customerService.forgetProfileByEmailOrMobile(userDetail); 
				//customerService.forgetProfile(resetProfile);
		GenericResponseEntity<String> data = new GenericResponseEntity<String>(result, 200);
		return new ResponseEntity<GenericResponseEntity<String>>(data, HttpStatus.OK);
	}
	
	@GetMapping(value="/forgetPasswordOTP")
	public ResponseEntity<GenericResponseEntity<String>> forgetProfileOTP(@RequestParam("phone") String phone){
		logger.info("========================= "+ phone);
		String result  = customerService.forgetProfileByEmailOrMobile(phone); 
				//customerService.forgetProfile(resetProfile);
		GenericResponseEntity<String> data = new GenericResponseEntity<String>(result, 200);
		return new ResponseEntity<GenericResponseEntity<String>>(data, HttpStatus.OK);
	}
	
	
	// Change Passwrod
	@PostMapping(value="/resetPassword")
	public ResponseEntity<GenericResponseEntity<String>> resetPassword(
			@RequestHeader(name="Autherization", required=true) String Autherization,
			@RequestBody Map<String, String> map){
		
		if( Autherization == null) {
			GenericResponseEntity<String> data = new GenericResponseEntity<String>(ServiceStatus.FAILED.name());
			
			return new ResponseEntity<GenericResponseEntity<String>>(data, HttpStatus.OK);
		}
		logger.info(" ======= JSON ====== {}", map.toString() );
		String result  = customerService.resetPassword(map, Autherization);
		
		GenericResponseEntity<String> data = new GenericResponseEntity<String>(result);
		
		return new ResponseEntity<GenericResponseEntity<String>>(data, HttpStatus.OK);
	}
	
	@GetMapping(value="/activateProfile/{token}")
	public ResponseEntity<GenericResponseEntity<String>> activateProfileByEmail(@PathVariable("token") String token, @RequestParam("key") String mobile){
		String result  = customerService.activateProfileByEmail(token, mobile);
		GenericResponseEntity<String> data = new GenericResponseEntity<String>(result);
		
		return new ResponseEntity<GenericResponseEntity<String>>(data, HttpStatus.OK);
	}
	
	
	@GetMapping(value="/activateAccountbyOTP")
	public ResponseEntity<GenericResponseEntity<Map<String, String> >> activateAccountbyOTP(
			@RequestParam(name="mobileNo", required=true) String mobileNo,
			@RequestParam(name="sessionId", required=true)String sessionId,
			@RequestParam(name="otp",required=true) String otp,
			@RequestParam(name="pwd",required=false) String pwd) {
		
		Map<String, String> result = customerService.activateAccountbyOTP(mobileNo,sessionId, otp, pwd);
		GenericResponseEntity<Map<String, String> > data = new GenericResponseEntity<Map<String, String> >(result, ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<Map<String, String> >>(data, HttpStatus.OK);
		
	}
	
	@GetMapping(value="/profileDetails")
	public  ResponseEntity<GenericResponseEntity<Customer> > profileDetails(@RequestParam(value="emailOrMobile") String emailOrMobile){
		Customer customer = customerService.getProfileDetail(emailOrMobile);
		GenericResponseEntity<Customer> customerData = new GenericResponseEntity<Customer>(customer, ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<Customer> >(customerData, HttpStatus.OK); 
	}
	
	// To-Do
	// Verify token and UserName combination. Valide token with other username, email, mobile can also be attempted
	// To-DO Encryption of Data
	@PostMapping("/walletBalance")
	public ResponseEntity<ZeroxPaymentDetails> getBalance(@RequestBody ZeroxPaymentDetails wallet){
		if(paymentService.validateOwnerOfWallet(wallet)) {
			wallet =  paymentService.getWalletBalance(wallet);
		
		return new ResponseEntity<ZeroxPaymentDetails>(wallet, HttpStatus.OK);
		}
		return new ResponseEntity<ZeroxPaymentDetails>(new ZeroxPaymentDetails(), HttpStatus.BAD_REQUEST);
	}
	
	// Return permissible machines only
	@GetMapping("/getContrbutorMachines")
	public ResponseEntity<GenericResponseEntity<ContrbutorMachines>> getContrbutorMachines(@RequestParam("emailId") String emailId){
		
		ContrbutorMachines contrbutorMachines = customerService.getContrbutorMachines(emailId);
		GenericResponseEntity<ContrbutorMachines> datacontrbutorMachines = new GenericResponseEntity<ContrbutorMachines>(contrbutorMachines, ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<ContrbutorMachines>>(datacontrbutorMachines, HttpStatus.OK);
	}
	
	/* ActivateForExistingProfile
	 * if email and Phone is already exist but profile is disabled: 
	 * In this case on board user by sending OTP and Emails. 
	 */
	
	@GetMapping("/activateForExistingProfile")
	 ResponseEntity<Factor2OTPResponse>  activateForExistingProfile(@RequestParam("emailIdOrMobile") String emailId){
		Factor2OTPResponse factor2OTPResponse = customerService.activateForExistingProfile(emailId);
		GenericResponseEntity<Factor2OTPResponse> dataFactor2OTPResponse = new GenericResponseEntity<Factor2OTPResponse>(factor2OTPResponse, ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<Factor2OTPResponse>(factor2OTPResponse, HttpStatus.OK);
	}
	
	// refer for new user. When referred user login and activate profile. referral amount will be credit
    @PostMapping("/referralToFriend")
    public ResponseEntity<GenericResponseEntity<String>> referralToFriend(@RequestBody List<String> friendList, @RequestParam("emailId") String emailId){
    	customerService.referralToFriend(friendList, emailId);
    	return new ResponseEntity<GenericResponseEntity<String>>(new GenericResponseEntity(ServiceStatus.SUCCESS.name(), ServiceStatus.SUCCESS.name(), HttpStatus.OK.value()), HttpStatus.OK);
    }
    
    /*
     * get Wallet Detail by emailId
     */
    @GetMapping("/walletDetails")
    public ResponseEntity<GenericResponseEntity<WalletModel>> getWalletDetails(
    		@RequestHeader(name="Authorization", required=true)
    		@RequestParam(name="emailId",required=true) String emailId){
    	
    	// FIXME
    	
    	// validate emailId and EmailId from Token is match or not.
    	
    	WalletModel wallet  = customerService.getWalletDetails(emailId);
    	
    	GenericResponseEntity<WalletModel> result = new GenericResponseEntity<WalletModel>(wallet, ServiceStatus.SUCCESS.name(), 200);
    	return new ResponseEntity<GenericResponseEntity<WalletModel>>(result, HttpStatus.OK);
    }
 
    @GetMapping("/existprofile")
    public ResponseEntity<GenericResponseEntity<Map<String, Object>>> existprofile(
    		@RequestParam(name="data",required=false) String data,
    		@RequestParam(name="type",required=false) String type){
    	Map<String, Object> map = new HashMap<>();
    	map = customerService.existprofile(data,type);
    	GenericResponseEntity<Map<String, Object>> result = new GenericResponseEntity<Map<String, Object>>(map);
    	return new  ResponseEntity<GenericResponseEntity<Map<String, Object>>>(result, HttpStatus.OK);
    	
    }
 
}
