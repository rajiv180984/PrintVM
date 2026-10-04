package com.first.test.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.test.exception.DataValidationException;
import com.first.test.exception.InvalidDataException;
import com.first.test.exception.PrintRequestValidationException;
import com.first.test.model.Customer;
import com.first.test.model.PrintCostPaymentRequest;
import com.first.test.model.GeoLocation;
import com.first.test.model.MachineDetails;
import com.first.test.model.PrintDocRequest;
import com.first.test.model.PrintDocResponse;
import com.first.test.model.PrintJobStatusUpdate;
import com.first.test.model.PrintProperties;
import com.first.test.model.PublicDocDetails;
import com.first.test.model.ShareDocForMachineByProfessionals;
import com.first.test.service.AdminService;
import com.first.test.service.CostDerivativeService;
import com.first.test.service.OfferService;
import com.first.test.service.PaymentService;
import com.first.test.service.UserPrintDocService;
import com.first.test.util.GenericResponseEntity;
import com.first.test.util.RequestType;
import com.first.test.util.ServiceStatus;
import com.google.gson.Gson;


@RestController
@RequestMapping(value = "/first/dream")
//@MultipartConfig(maxFileSize = 1024*1024*1024, maxRequestSize = 1024*1024*1024, location="/temp")
public class SmartOfficeServiceController {

	private Logger logger = LoggerFactory.getLogger(SmartOfficeServiceController.class);

	@Autowired
	UserPrintDocService userPrintDocService;

	@Autowired
	CostDerivativeService costDerivativeService;

	@Autowired
	AdminService adminService;

	@Autowired
	PaymentService paymentService;
	
	@Autowired
	OfferService offerService;
	

	@Autowired 
	ShareDocService shareDocService;
	
	@Autowired
	Gson gson;

	// @RequestParam("file") MultipartFile file
	/**
	 * @param file
	 * @param machineId
	 * @param mobileNo
	 * @param emailId
	 * @param param
	 * @param couponcode
	 * @return
	 * recieveDoc
	 */
	/*
	 *
	 * Remote Print ---> Remote SmartShare
	 */
	@PostMapping(value = "/remoteSmartShare")
	public ResponseEntity<GenericResponseEntity<String>>  getPrintDocRequest(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "machineId", required = true) String machineId,
			@RequestParam(value = "printJobId", required = true) String printJobId,
			@RequestParam(value = "emailId", required = true) String emailId,
			@RequestParam(value = "printParam", required = true) String printParam,
			@RequestParam(value = "recipientMobile", required = true) String recipientMobile,
			@RequestParam(value = "couponcode", required = false) String couponcode) {

		try {
			String printRequestStr = URLDecoder.decode(printParam, StandardCharsets.UTF_8.name());
			logger.info("==== Remote Share ==={}",printRequestStr);
			PrintProperties printProperties = gson.fromJson(printRequestStr, PrintProperties.class);
			logger.info("==== Parsed Data ==={}",printProperties.toString());
			
			PrintDocRequest printRequest = JsonPojoMapper(machineId, printJobId, emailId,couponcode,null, printProperties);

			//printRequest.setSharedForEmail(recipientMobile);
			printRequest.setSharedForMobile(recipientMobile);

			logger.info("================="+ printRequest.toString());
			
			printRequest.setRequestType(RequestType.REMOTE.name());
			
			userPrintDocService.persistRemotePrintRequest(machineId, printRequest, file);
			printRequestAndPrintResponse(printRequest);
			GenericResponseEntity<String> result = new GenericResponseEntity("SUCCESS", ServiceStatus.SUCCESS.name(), 200);

            return new ResponseEntity<GenericResponseEntity<String>>(result, HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			logger.error(" Failed to accept Request", e);
			
		}
		 return new ResponseEntity<GenericResponseEntity<String>>(new GenericResponseEntity("FAIL", ServiceStatus.SUCCESS.name(), 200) , HttpStatus.OK);
	}
	
	/*
	 * Direct Print: File directly send to machine over wifi or Bluetooth
	 * Only Print command trigger through Cloud to Avoid hacking and tempering
	 */
	
	/*
	 * DoC should be created at machine
	 */
	@PostMapping(value = "/directprint")
	public ResponseEntity<PrintDocRequest> directPrint(@RequestBody PrintDocRequest printRequest) {

		if (printRequest.getPrintJobId() == null) {
			throw new PrintRequestValidationException("docId can not be empty");
		}
	
			printRequest.setRequestType(RequestType.DIRECT.name());
			userPrintDocService.persistPrintRequest(printRequest.getMachineId(), printRequest, null);
			printRequestAndPrintResponse(printRequest);
			return new ResponseEntity<PrintDocRequest>(printRequest, HttpStatus.OK);
		
	}
	
	/*
	 * If Payment is Pending then request already persist in DB and Recall the print request and Complete request based on 
	 * Payment status. Similar to Adding to cart and asking for payment
	 * Print ---> SmartShare
	 */
	@PostMapping(value = "/smartSharedocafterpayment")
	public ResponseEntity<PrintDocResponse> printdocafterpayment(@RequestBody PrintDocRequest printDocRequest,
			@RequestParam(value = "machineId", required = true) String machineId,
			@RequestParam(value = "mobileNo", required = true) String mobileNo,
			@RequestParam(value = "emailId", required = true) String emailId) {

		try {
		
			printDocRequest = userPrintDocService.printPaymentPendingRequest(machineId, printDocRequest);
			PrintDocResponse printDocResponse = printRequestAndResponaseMapping(printDocRequest);
			return new ResponseEntity<PrintDocResponse>(printDocResponse, HttpStatus.OK);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<PrintDocResponse>(new PrintDocResponse(), HttpStatus.BAD_GATEWAY);
	}


	@PostMapping(value = "/costDerivative")
	public ResponseEntity<GenericResponseEntity<PrintCostPaymentRequest>> getPrintCost(
			@Valid @RequestBody PrintCostPaymentRequest docCostEvaluatorDeviceRequest) {
		logger.info("======== Cost Derivetives ==========={}", docCostEvaluatorDeviceRequest.toString());
		if (docCostEvaluatorDeviceRequest.getPrintExprn() == null
				|| docCostEvaluatorDeviceRequest.getPrintExprn().isEmpty()) {
			throw new InvalidDataException("Invalid printExprn values ");
		}
		PrintCostPaymentRequest costDerivative = costDerivativeService.evaluateCost(docCostEvaluatorDeviceRequest);
		GenericResponseEntity<PrintCostPaymentRequest> rsult = new GenericResponseEntity<PrintCostPaymentRequest>(
				costDerivative, ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<PrintCostPaymentRequest>>(rsult, HttpStatus.OK);
	}


	@PostMapping(value = "/test", consumes = { "multipart/form-data" })
	public ResponseEntity<PrintDocResponse> testAPI(@RequestParam("mobileNo") String mobileNo,
			@RequestParam("uploadingFiles") MultipartFile uploadingFiles) {

		System.out.println("======================= " + mobileNo);

		// successfull All Print doc details must share to user

		// userPrintDocService.persistPrintRequest();

		return new ResponseEntity<PrintDocResponse>(new PrintDocResponse(), HttpStatus.OK);
	}

	// share print doc to Friend to specific person
	@PostMapping(value = "/shareprintdoc")
	public ResponseEntity<PrintDocResponse> shareprintdoc(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "machineId", required = false) String machineId,
			@RequestParam(value = "mobileNolist", required = true) String mobileNolist,
			@RequestParam(value = "mobileOfOwnerOfdoc", required = true) String mobileOfOwnerOfdoc) {

		userPrintDocService.shareprintdoc(machineId, mobileNolist, mobileOfOwnerOfdoc, file);
		return new ResponseEntity<PrintDocResponse>(new PrintDocResponse(), HttpStatus.OK);
	}

	// Secure at Particular locations only Lets say University Specific 
	@PostMapping(value = "/shareSmartdocOnMachinebyContributor")
	public ResponseEntity<GenericResponseEntity<String>> shareprintdocOnlocation(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "docProp", required=true) String docProp) {

		logger.info(" Shared Doc ================ {}", docProp.toString());
		try {
			 docProp = URLDecoder.decode(docProp, StandardCharsets.UTF_8.name());
			 
			//machineIdList = URLDecoder.decode(machineIdList, StandardCharsets.UTF_8.name());
			//mobileNoList = URLDecoder.decode(mobileNoList, StandardCharsets.UTF_8.name());
			//docTitle = URLDecoder.decode(docTitle, StandardCharsets.UTF_8.name());
			ShareDocForMachineByProfessionals shareDocForMachineByProfessionals = gson.fromJson(docProp, ShareDocForMachineByProfessionals.class);
			//shareDocService.shareprintdocOnlocation(machineIdList,  securecode, docTitle,
				//	 emailIdOfOwnerOfdoc, categoryName, subCategory, file);
			shareDocService.shareprintdocOnlocation(shareDocForMachineByProfessionals, file);
			return new ResponseEntity<GenericResponseEntity<String>>(new GenericResponseEntity(ServiceStatus.SUCCESS.name()), HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<GenericResponseEntity<String>>(new GenericResponseEntity(ServiceStatus.FAILED.name()), HttpStatus.OK);
	}

	// get all document metadata along with S3 Path of doc , category etc.
	@GetMapping(value = "/allPublicDocByCategory")
	public ResponseEntity<List<PublicDocDetails>> allPublicDocByCategory(
			@RequestParam(value = "category", required = false, defaultValue="ALL") String category) {
		List<PublicDocDetails> docList = adminService.getPublicDocByCategory(category);
		return new ResponseEntity<List<PublicDocDetails>>(docList,HttpStatus.OK);

	}
	
	
	// get All Doc by Machine Id
	@GetMapping("/getAllDocSpecificToMachine")
	public List<PublicDocDetails> getAllDocSpecificToMachine(@RequestParam(name="machineId")String machineId){
		List<PublicDocDetails> list = adminService.getPublicDocSpecificToMachine(machineId);
		return list;
	}

	// Get the nearby all printers
	@GetMapping(value = "getallnearbyOffice")
	public ResponseEntity<List<MachineDetails>> getAllNearbyPrinter(@RequestParam("lat") double lat, @RequestParam("longt") double longt) {

		List<Double> coordinates = new ArrayList<>();
		coordinates.add(lat);
		coordinates.add(longt);
		GeoLocation geoLocation = new GeoLocation();
		geoLocation.setCoordinates(coordinates);
		geoLocation.setType("POINT");
		
		List<MachineDetails> machineDetails = adminService.getAllNearbyPrinter(geoLocation);

		return new ResponseEntity<List<MachineDetails>>(machineDetails, HttpStatus.OK);
	}

	// search machine by area name
	@GetMapping(value = "searchbyarea")
	public ResponseEntity<List<MachineDetails>> getAllPrinterByAreaName(@RequestParam("areaName") String areaName) {

		List<MachineDetails> machineDetails = adminService.getAllPrinterByAreaName(areaName);

		return new ResponseEntity<List<MachineDetails>>(machineDetails, HttpStatus.OK);
	}

	
	@GetMapping(value = "/isValidCoupon")
	public ResponseEntity<String> isValideCoupon(@RequestParam(value="userId") String userId, @RequestParam(value="promoCode")String promoCode)
	{
		
		float validValue = offerService.getValidatePromocodeAndValue(userId, promoCode);
		if(validValue <= 0)
			return new ResponseEntity<String>("INVALID", HttpStatus.OK);
		
		return new ResponseEntity<String>(String.valueOf(validValue), HttpStatus.OK);
			
	}
	
	private void printRequestAndPrintResponse(PrintDocRequest printRequest) {
		// to-Do remove few details
	}

	@PostMapping("/testing")
	public ResponseEntity<String> mappingOfData(@RequestBody String print) {
		System.out.println("========= "+print);
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map = objectMapper.readValue(print, map.getClass());
			System.out.println("======Request ====== "+map.get("requestType"));
			return new ResponseEntity<String>("ZEROPRINT",HttpStatus.OK);
		} catch (Exception e) {
			/*try {
				PartnerItemRequest printDocRequest = objectMapper.readValue(message.getPayload(), PartnerItemRequest.class);
				return "PARTNERPRINT";
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			e.printStackTrace();
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
		
	}
	
	// print the Document shared from Remote.
	@PostMapping(value="/printlaterCommand")
	public ResponseEntity<String> publishCommand(@RequestBody PrintDocRequest printDocRequest) {
		userPrintDocService.publishPrintCommandByOwnerOfDoc(printDocRequest);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	//get List of all Pending Print Request
	/*
	 * Print --> ShareDoc
	 */
	@GetMapping(value="/getPendingPrintRequests")
	public List<PrintDocRequest> getPendingPrintRequests(
			@RequestParam(name="emailId") String emailId,
			@RequestParam(name="mobileNo") String mobileNo
			) {
		
		List<PrintDocRequest> pendingList = new ArrayList<>();
		pendingList = userPrintDocService.getPendingPrintRequests(emailId, mobileNo);
		return  pendingList;
	}
	
	// print shared Doc where actual File will not be pass only docId will be pass
	/*
	 * print --> smart
	 */
	@PostMapping(value="/smartsharedDoc")
	public ResponseEntity<String>  printsharedDoc(@RequestBody PrintDocRequest printDocRequest) {
		userPrintDocService.printsharedDoc(printDocRequest);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	//GeoLocation
	
	// search machine by area
	@PostMapping(value = "searchByGeoLocation")
	public ResponseEntity<List<MachineDetails>> searchMachinesByGeoLocation(@RequestBody GeoLocation geoLocation) {

		List<MachineDetails> machineDetails = adminService.searchMachinesByGeoLocation(geoLocation);

		return new ResponseEntity<List<MachineDetails>>(machineDetails, HttpStatus.OK);
	}
	
	

	private PrintDocResponse printRequestAndResponaseMapping(PrintDocRequest printRequest) {
		// TODO Auto-generated method stub
		return new PrintDocResponse();
	}

	private PrintDocRequest JsonPojoMapper(String machineId, String PrintJobId, String emailId, String promocode, String docId,
			PrintProperties printProperties) {
		PrintDocRequest printRequest = new PrintDocRequest();
		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();

		System.out.println("=====JSON To Poja Mapper ================ Test " + printProperties.toString());
		if(PrintJobId == null) {
			PrintJobId = randomUUIDString;
		}
		
		
		printRequest.setPrintJobId(PrintJobId);
		printRequest.setOrderId(PrintJobId);
		printRequest.setMachineId(machineId);
		printRequest.setEmailId(emailId);
		printRequest.setPrintProperties(printProperties);
		printRequest.setPromocode(promocode);
		printRequest.setPrintStatus("PENDING");
		return printRequest;
	}

	/*
	 * Search: doc Search by doc name: Title, Category, FileName
	 */
	@GetMapping(value = "/docSearch")
	public ResponseEntity<List<PublicDocDetails>> docSearch(
			@RequestParam(value = "srcText", required = false, defaultValue="*") String srcText) {
		List<PublicDocDetails> docList = adminService.docSearch(srcText);
		return new ResponseEntity<List<PublicDocDetails>>(docList,HttpStatus.OK);

	}

	// GET URL for upload doc in S3 Bucket
	@GetMapping("/getUploadURL")
	public String getUploadURL(
			@RequestHeader(name = "Authorization", required=true) String Authorization,
			@RequestParam("printJobId") String printJobId,
			@RequestParam("printJobId") String emailId) {
		return userPrintDocService.getUploadS3URL(printJobId,emailId);
		
	}
	
	// List all shared Doc by emailId
	@GetMapping("/allsharedDocOfContributor")
	public ResponseEntity<GenericResponseEntity<List<ShareDocForMachineByProfessionals>>> allsharedDocOfContributor(
			@RequestHeader(name="Authorization", required=true) String Authorization,
			@RequestParam(name="emailId", required=true) String emailId){
		logger.info("List of Doc by EmailID {}", emailId);
		List<ShareDocForMachineByProfessionals> listDoc = shareDocService.allsharedDocOfContributor(emailId);
		GenericResponseEntity<List<ShareDocForMachineByProfessionals>> listDocRes = new GenericResponseEntity<List<ShareDocForMachineByProfessionals>>
		(listDoc, ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<List<ShareDocForMachineByProfessionals>>>(listDocRes,HttpStatus.OK);
		
	}
	
	@GetMapping("/printJobHistory")
	public ResponseEntity<GenericResponseEntity<List<PrintDocRequest>>> printJobHistory(@RequestHeader(name ="Authorization", required=true) String Authorization
			,@RequestParam(name="emailId", required=true) String emailId
			,@RequestParam(name="status", required=false) String status
			,@RequestParam(name="requestType", required=false) String requestType){
		
		logger.info("======= PrintJob History ======={}", emailId);
		List<PrintDocRequest> list = userPrintDocService.printJobHistory(emailId, status, requestType);
		GenericResponseEntity<List<PrintDocRequest>> listRes = new GenericResponseEntity<List<PrintDocRequest>>(list, ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<List<PrintDocRequest>>>(listRes, HttpStatus.OK);
		
	}
	
	@GetMapping("/remotePrintTrigger")
	public ResponseEntity<GenericResponseEntity<PrintDocRequest>> remotePrintTrigger(
			@RequestHeader(name = "Authorization", required = true) String Authorization,
			@RequestParam(name = "printjobId", required = true) String printjobId,
			@RequestParam(name = "machineId", required = true) String machineId

	) {

		logger.info("========Remote Print ");
		PrintDocRequest request = userPrintDocService.remotePrintTrigger(printjobId, machineId);
		GenericResponseEntity<PrintDocRequest> reult = new GenericResponseEntity<PrintDocRequest>(request,
				ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<PrintDocRequest>>(reult, HttpStatus.OK);

	}

}
