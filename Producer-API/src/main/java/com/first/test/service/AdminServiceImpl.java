package com.first.test.service;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.aspectj.weaver.ast.And;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.cloudformation.model.AlreadyExistsException;
import com.fasterxml.uuid.Generators;
import com.first.test.aws.S3BucketAccess;
import com.first.test.encrypt.AESUtils;
import com.first.test.exception.InsufficientPriviledgeException;
import com.first.test.exception.InvalidBulkPrintException;
import com.first.test.exception.InvalidDataException;
import com.first.test.exception.NoRecordFoundException;
import com.first.test.exception.OTPAuthenticationException;
import com.first.test.model.BulkOrderCostEvaluation;
import com.first.test.model.BulkPrintOrder;
import com.first.test.model.BusinessType;
import com.first.test.model.Factor2OTPResponse;
import com.first.test.model.GeoLocation;
import com.first.test.model.MachineDetails;
import com.first.test.model.OTPRecord;
import com.first.test.model.PrintCostPaymentRequest;
import com.first.test.model.PrintDocRequest;
import com.first.test.model.PrintProperties;
import com.first.test.model.PublicDocDetails;
import com.first.test.model.ShareDocForMachineByProfessionals;
import com.first.test.model.SharedDocForUser;
import com.first.test.model.WIFIMachine;
import com.first.test.model.WalletModel;
import com.first.test.model.ZeroxPaymentDetails;
import com.first.test.util.AES;
import com.first.test.util.ErrorCodeNo;
import com.first.test.util.LAYOUT;
import com.first.test.util.PageCountUtility;
import com.first.test.util.PaymentStatus;
import com.first.test.util.PrintStatus;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;

import groovyjarjarpicocli.CommandLine.Help.Layout;

@Service
public class AdminServiceImpl implements AdminService {

	private static final String DELIM = "@@@";

	private static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
	private static HashMap<String, WIFIMachine> machineKeyMap = new HashMap<>();

	@Autowired
	MongoTemplate mtemplate;

	@Autowired
	S3BucketAccess s3BucketAccess;

	@Autowired
	ValidationService validationService;
	
	@Autowired
	CustomerProfileService customerService;
	
	@Autowired
	CostDerivativeService costDerivativeService;
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	SMSSendService smsSendService;
	
	@Autowired
	AESUtils aesUtils;

	@Value("${zerox.bulkorder.pagethresold}")
	private int minPageCount;
	
	@Autowired
	UserPrintDocService userPrintDocService;

	@Override
	public String addPublicDocument(MultipartFile file, PublicDocDetails docDetailsJson) {
		// TODO Auto-generated method stub
		// persist in Central DB
		// Set Machine ID = PUBLIC

		// No need to send to machine. Based on URL public doc are visible

		// machine and document mapping.

		return "OK";

	}

	@Override
	public PublicDocDetails publicdoctomachines(MultipartFile file, PublicDocDetails publicDocDetails) {
		// TODO Auto-generated method stub
		// persist in S3 and get s3Path

		if (publicDocDetails.getOwnerEmailId() == null) {
			logger.error(" Insufficient Priviledge ");
			throw new InsufficientPriviledgeException(" Insufficient Priviledge ");
		}

		if (!validationService.checkPrivildge(publicDocDetails.getOwnerEmailId(),
				publicDocDetails.getOwnerMobileNo())) {

		}
		String docId = Generators.timeBasedGenerator().generate().toString();
		publicDocDetails.setDocId(docId);
		// To-Do
		// Add machine restriction for Share doc

		try {
			String s3Path = s3BucketAccess.persistPublicDoc(file, publicDocDetails.getCategory());
			publicDocDetails.setS3path(s3Path);
			logger.info("=============S3 Pth ===================== " + s3Path);
			if (publicDocDetails.getMachineList() == null || publicDocDetails.getMachineList().size() == 0) {
				// it is public and available for everyone
				publicDocDetails.setMachineId("ALL");
				publicDocDetails.setPublic(true);
				logger.info("=================================== " + publicDocDetails.toString());
			} else if (publicDocDetails.getMachineList().size() > 0) {
				// to-do batch update
				List<String> machineList = publicDocDetails.getMachineList();
				logger.info("Size of MachineList " + machineList);
				PublicDocDetails docDetails = null;
				List<PublicDocDetails> publicDocList = new ArrayList<>();
				for (String machineId : machineList) {
					docDetails = new PublicDocDetails();
					docDetails.setDocId(UUID.randomUUID().toString());
					docDetails.setCategory(publicDocDetails.getCategory());
					docDetails.setDesc(publicDocDetails.getDesc());
					docDetails.setS3path(s3Path);
					docDetails.setMachineId(machineId);
					docDetails.setName(publicDocDetails.getName());
					docDetails.setSubCategory(publicDocDetails.getSubCategory());
					docDetails.setPublic(true);
					publicDocList.add(docDetails);
					mtemplate.save(docDetails);

				}
				BulkOperations buldOps = mtemplate.bulkOps(BulkMode.UNORDERED, PublicDocDetails.class);
				buldOps.insert(publicDocList);
				logger.info("Public Doc ");
			}
			mtemplate.save(publicDocDetails);
		} catch (AlreadyExistsException e) {
			logger.error(" Already Exist ");
			throw e;
		} catch (Exception e) {
			logger.error("Failed to publish ", e);
		}
		return publicDocDetails;
	}

	@Override
	public List<MachineDetails> getAllNearbyPrinter(GeoLocation geoLocation) {
		// db.machineDetails.find({geoLocation:{ $near:{$geometry: { type: "Point",
		// coordinates: [ -73.9667, 40.78 ] }, $minDistance: 1000,$maxDistance: 50000}}}
		// );

		MongoCollection<Document> machineDetailsColl = mtemplate
				.getCollection(mtemplate.getCollectionName(MachineDetails.class));

		// String template = "Hi ${name}! Your number is ${number}";

		String teplateQuery = "{geoLocation:{ $near:{$geometry: { type: \"Point\",  coordinates: [ ${lat}, ${longt} ] }, $minDistance: 1,$maxDistance: 50000}}}";

		Map<String, String> data = new HashMap<String, String>();
		data.put("lat", String.valueOf(geoLocation.getCoordinates().get(0)));
		data.put("longt", String.valueOf(geoLocation.getCoordinates().get(1)));

		String formattedString = StrSubstitutor.replace(teplateQuery, data);
		BasicQuery query1 = new BasicQuery(formattedString);
		query1.fields().include("machineId").include("machineAddress").include("geoLocation");
		List<MachineDetails> list = mtemplate.find(query1, MachineDetails.class);
		System.out.println(" ============ " + list.size());
		return list;
	}

	@Override
	public List<MachineDetails> getAllPrinterByAreaName(String areaName) {
		// TODO Auto-generated method stub
		Criteria regex = Criteria.where("machineAddress").regex(".*" + areaName + "0*", "i");
		Criteria regexCityName = Criteria.where("cityName").regex("^" + areaName + "$", "i");
		Criteria crit = new Criteria().orOperator(regex, regexCityName);
		Query query = new Query().addCriteria(crit);
		query.fields().include("machineId").include("machineAddress").include("cityName").include("geoLocation");
		List<MachineDetails> list = mtemplate.find(query, MachineDetails.class);
		// check this name text search query in address
		// List<MachineDetails> listMachineDetails= mtemplate.query(domainType)
		// check this name text search query in cityName matching
		// List<MachineDetails> listMachineDetails= mtemplate.query(domainType)
		// combined both result
		return list;
	}

	@Override
	public List<PublicDocDetails> getPublicDocByCategory(String category) {
		// TODO Auto-generated method stub
		logger.info(" ========== Get Pulic doc ======== ");
		List<PublicDocDetails> allDocList = new ArrayList<>();
		if (category.equals("ALL")) {
			allDocList = mtemplate.findAll(PublicDocDetails.class);
		} else {
			new Criteria();
			Query query = new Query(
					Criteria.where("category").is(category).andOperator(Criteria.where("machineId").is("ALL")));
			allDocList = mtemplate.find(query, PublicDocDetails.class);
			logger.info("===Total public doc " + allDocList.size());
		}
		return allDocList;
	}

	@Override
	public List<PublicDocDetails> getPublicDocSpecificToMachine(String machineId) {
		Query query = new Query(new Criteria().where("machineId").is(machineId));
		query.fields().exclude("s3path");
		List<PublicDocDetails> list = mtemplate.find(query, PublicDocDetails.class);
		return list;
	}

	@Override
	public List<MachineDetails> searchMachinesByGeoLocation(GeoLocation geoLocation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllCategory() {

		List<String> categoryList = new ArrayList<>();
		Bson filterByPermLink = Filters.eq("machineId", "ALL");

		// mtemplate.getCollection("PublicDocDetails").distinct("category",
		// String.class).into(categoryList);
		// DistinctIterable<String> sharedlist =
		mtemplate.getCollection("ShareDocForMachineByProfessionals").distinct("category", String.class)
				.filter(filterByPermLink).into(categoryList);

		categoryList.add("Others");

		return categoryList;
	}

	@Override
	public List<String> getAllCategoryByMachineId(String machineId) {

		List<String> categoryList = new ArrayList<>();
		/*
		 * if(machineId == null || machineId.isEmpty()) {
		 * mtemplate.getCollection("ShareDocForMachineByProfessionals").distinct(
		 * "category", String.class).into(categoryList); }else {
		 */
		Bson filterByPermLink = Filters.eq("machineId", machineId);
		mtemplate.getCollection("ShareDocForMachineByProfessionals").distinct("category", String.class)
				.filter(filterByPermLink).into(categoryList);
		// }
		categoryList.add("Others");
		return categoryList;
	}

	@Override
	public ZeroxPaymentDetails testAddWalletBalance(ZeroxPaymentDetails zeroxPaymentDetails) {
		mtemplate.save(zeroxPaymentDetails);
		return zeroxPaymentDetails;
	}

	@Override
	public List<ShareDocForMachineByProfessionals> getDocListByCategory(String category, String machineId) {

		Query query = new Query(
				Criteria.where("category").is(category).andOperator(Criteria.where("machineId").is("ALL")));
		return mtemplate.find(query, ShareDocForMachineByProfessionals.class);

	}

	@Override
	public List<ShareDocForMachineByProfessionals> getDocListByCategoryAndMachineId(String category, String machineId) {
		/*
		 * Criteria criteria= new Criteria().where("cateory").is(category); Criteria
		 * criteria1Machine= new Criteria().where("machineId").is(machineId); Criteria
		 * crit = new Criteria(); crit.orOperator(criteria, criteria1Machine );
		 * 
		 */
		Query query = new Query(
				Criteria.where("category").is(category).andOperator(Criteria.where("machineId").is(machineId)));
		return mtemplate.find(query, ShareDocForMachineByProfessionals.class);
	}

	// https://spring.io/blog/2014/07/17/text-search-your-documents-with-spring-data-mongodb
	@Override
	public List<PublicDocDetails> docSearch(String srcText) {
		// TODO Auto-generated method stub
		/*
		 * TextIndexDefinition textIndex = new TextIndexDefinitionBuilder()
		 * .onField("title", 2F) .onField("content") .build();
		 */
		TextCriteria criteria = TextCriteria.forDefaultLanguage().matchingAny("coffee", "cake");
		// Set for docId

		return null;
	}

	@Override
	public BulkPrintOrder buldOrder(MultipartFile file, MultipartFile coverPage, BulkPrintOrder bulkOrder) {
		try{
			// call API to evaluate Cost of Order
			bulkOrder = EvaluateOrderCost(bulkOrder);
			//check wallet amount
			WalletModel wallet = customerService.getWalletDetails(bulkOrder.getEmailId());
			if(wallet.getBalance()>=  bulkOrder.getBuldOrderCost().getOrderCost()) {
				bulkOrder.setSufficientAmount(true);
			}else {
				bulkOrder.setSufficientAmount(false);	
			}
		bulkOrder.setOrderType("BULK");
		bulkOrder.setOrderId(UUID.randomUUID().toString());

		// Integrate Logistic APIs

		// Evaluate Partner who take printing service
		
		// Notify the Bulk Print Partner
		bulkOrder.setOrderStaus("CHECKOUT");
		bulkOrder.setS3filePath("s3://aws.amazon.com/zeroxvm/bulkfile.pdf");
		
		// Persist Data DB
		mtemplate.save(bulkOrder);
		logger.info("======= Saved Successfully =======");
		return bulkOrder;
		}catch (Exception e) {
			logger.error(" Failed to Persist data", e);
		}
      return null;
	}

	private BulkPrintOrder EvaluateOrderCost(BulkPrintOrder bulkOrder) {
		PrintProperties prop = bulkOrder.getPrintProperties();
		// FIXME for ALL page 
		int pageCount = PageCountUtility.getTotalCountOfPrintPage(prop.getPexpn(), null);
		if(pageCount == 0) {
			throw new InvalidBulkPrintException("Incorrect Page number");
		}
		if (pageCount*bulkOrder.getNoOfCopy() < minPageCount) {
			throw new InvalidBulkPrintException(" request is not valid for bulk order ");
		}
		bulkOrder.getPrintProperties().setPageCount(pageCount);
		bulkOrder.setTotalPageCount(pageCount* bulkOrder.getNoOfCopy());
		prop.setPageCount(pageCount);
		bulkOrder.setPrintProperties(prop);
		
		BulkOrderCostEvaluation bulkorderCost = new BulkOrderCostEvaluation();
		
		float printCost = costDerivativeService.bulkPrintCost(pageCount, null, null) *  bulkOrder.getNoOfCopy();

		float coverPrintCost = 5.0f * bulkOrder.getNoOfCopy();
				//costDerivativeService.coverPrintCost();
		float bindingCost = costDerivativeService.getBindingCost(bulkOrder.getBinding())* bulkOrder.getNoOfCopy();;
		
		float totalPrintBindCost = printCost + coverPrintCost + bindingCost;
		float gst = totalPrintBindCost * 0.025f;
		
		float shippingCost = 50.0f;
		
		float orderCost = totalPrintBindCost + shippingCost + gst;
		
		
		bulkorderCost.setPrintCost(printCost);
		bulkorderCost.setCoverPrintCost(coverPrintCost);
		bulkorderCost.setBindingCost(bindingCost);
		bulkorderCost.setTotalPrintBindCost(totalPrintBindCost);
		bulkorderCost.setGst(gst);
		bulkorderCost.setShippingCost(shippingCost);
		bulkorderCost.setOrderCost(orderCost);
		bulkOrder.setBuldOrderCost(bulkorderCost);
		bulkOrder.setPaymentStatus(PaymentStatus.PENDING.name());
		return  bulkOrder;
	}

	private boolean validate(String orderId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double bulkPrintPricing(BulkPrintOrder bulkOrder) {

		int totalPaperCount = evaluatePaperCount(bulkOrder);

		return 0;
	}

	private int evaluatePaperCount(BulkPrintOrder bulkOrder) {
		// TODO Auto-generated method stub
		if (bulkOrder.getNoOfCopy() > 0) {
			// evaluate
		}

		return 0;
	}

	@Override
	@Transactional
	public WIFIMachine getWIFIMachineDetails(WIFIMachine wifiMachine, String printjobId, String emailId) {
		WIFIMachine wifi = null;
		WIFIMachine machineKeyFromDB = getMachineKeyFromDB(wifiMachine.getKey());
		if(machineKeyFromDB == null) {
			 throw new InvalidDataException("Tempred Request", ErrorCodeNo.NOTEXIST.getValue());
		}
		System.out.println("======= 1: "+machineKeyFromDB.toString());
		//wifiMachine.getMachineKey():  this MachineKey send from Android Device 
		String ssid = aesUtils.decryptMachine(wifiMachine.getMachineName(), machineKeyFromDB.getMachineKey());
		System.out.println(" =========== SSID "+ ssid);
		String pwd = aesUtils.decryptMachine(wifiMachine.getMachineName(), machineKeyFromDB.getMachineKey());
		System.out.println(" =========== SSID "+ pwd);
		String machineId = getMachineId(ssid);
        if(machineId == null || ssid == null || pwd == null) {
        	 throw new InvalidDataException("Tempred Request", ErrorCodeNo.NOTEXIST.getValue());
        }
		
		wifi = new WIFIMachine();
		wifi.setSsid(ssid);
		wifi.setMachineId(machineId);
		wifi.setWifipwd(pwd);
		wifi.setWifiIPAdrress(machineKeyFromDB.getWifiIPAdrress());
		
		// return preisgen url in case of Load from Cloud when WIFI is not working
		if (printjobId != null) {
			String randFileName = RandomStringUtils.randomAlphanumeric(8);
			String s3FilePath = machineId.concat("/").concat(randFileName);
			System.out.println(" S3file :" + s3FilePath);

			// Associate S3 path with Request

			URL url = s3BucketAccess.getS3PreSignedURLForUpload(s3BucketAccess.getBucketName(), s3FilePath,
					s3BucketAccess.getPresignedexpiration());
			logger.info(" =============== {}", url.getPath());
			wifi.setJobUrlforUpload(url.getPath());
		
			// create encryptionkey
			// fecth details of request by PrintJobId
			PrintCostPaymentRequest printCostPaymentRequest = getPrintRequestDetailsByPrintJobId(printjobId);
			logger.info(" print job : {}", printCostPaymentRequest );
			
			if(printCostPaymentRequest == null) {
				   throw new InvalidDataException("Tempred Request", ErrorCodeNo.NOTEXIST.getValue());
			}
			if(!printCostPaymentRequest.getEmailId().equalsIgnoreCase(emailId)) {
				   throw new InvalidDataException("Tempred Request", ErrorCodeNo.ALREADYPROCESSED.getValue());
			}
			
			  if(printCostPaymentRequest !=null && !printCostPaymentRequest.getStatus().equals(PrintStatus.NEW.name())){
		    	   throw new InvalidDataException("Request already requested", ErrorCodeNo.ALREADYPROCESSED.getValue());
		       }  
			  
			  // Case if user have valid key can be used multiple time
			  // hence attach timestamp as well as validate against db at machine
			   
			  // get the ScreteKey 
			  String encrypedKey = getEncryptedKey(machineId, printCostPaymentRequest);
			  wifi.setPublicCertKey(encrypedKey);
			  wifi.setMachineSecreteKeyForEncrypion(null);
			  wifi.setQrCodeFilePath(null);
			  wifi.setMachineKey(null);
			  wifi.setMachineKey(null);
			  
			  System.out.println(" Machine Details :"+ wifi.toString());
			  paymentService.executePaymentDeducted(printCostPaymentRequest, emailId);
			  
			/*  PrintDocRequest printDoc =   createPrintRequest(printCostPaymentRequest);
			  logger.info(" PrintJOB Request :{}", printDoc );
			  mtemplate.save(printDoc);
			*/
			  
	}
		
	    return wifi;

	}

	private PrintDocRequest createPrintRequest(PrintCostPaymentRequest printCostPaymentRequest) {
		PrintDocRequest printDoc = new PrintDocRequest();
		printDoc.setOrderId(printCostPaymentRequest.getPrintJobId());
		printDoc.setPrintJobId(printCostPaymentRequest.getPrintJobId());
		printDoc.setEmailId(printCostPaymentRequest.getEmailId());
		printDoc.setBalanceAmountToPay(0);
		printDoc.setCreateOn(System.currentTimeMillis());
		PrintProperties printProp = new PrintProperties();
		
		printProp.setColor(printCostPaymentRequest.isColor());
		printProp.setLayout(LAYOUT.portrait.name());
		printProp.setPageCount(printCostPaymentRequest.getNumberOfPage());
		printProp.setPexpn(printCostPaymentRequest.getPrintExprn());
		printProp.setpSize("A4");
		
		printDoc.setPrintProperties(printProp);
		printDoc.setMachineId(printCostPaymentRequest.getMachineId());
		printDoc.setBusinessType(BusinessType.ZEROX.name());
		printDoc.setPaymentStatus(PaymentStatus.COMPLETED.name());
		printDoc.setPrintCost((float)printCostPaymentRequest.getTotalCost() );
		printDoc.setPrintStatus(PrintStatus.PENDING.name());
		
	   	
		return printDoc;
	}

	private WIFIMachine getMachineKeyFromDB(String machineKey) {
		// TODO Auto-generated method stub
		// FIXME Fetch from Radis Cache
		System.out.println(" test 2:  "+machineKey );
		if(machineKeyMap.get(machineKey) != null) {
			return machineKeyMap.get(machineKey);
		}else {
			Query query = new Query(Criteria.where("encryptedWifiPassword").is(machineKey) );
			WIFIMachine wifiMachine = mtemplate.findOne(query, WIFIMachine.class);
			System.out.println(" test 3:  "+wifiMachine.toString() );
			if(wifiMachine == null) {
				logger.error("Incorrect WIFI Details :");
				return null;
			}
			return wifiMachine;
		}
		
	}

	private String getEncryptedKey(String machineId, PrintCostPaymentRequest printCostPaymentRequest) {
		StringBuilder builder = new StringBuilder(machineId);
		// machineId@@@printJobId@@@count@@@pexpn@@@timestamp
		builder.append(DELIM)
		.append(printCostPaymentRequest.getPrintJobId())
		.append(DELIM).append(printCostPaymentRequest.getNumberOfPage())
		.append(DELIM).append(printCostPaymentRequest.getPrintExprn())
		.append(DELIM).append(String.valueOf(System.currentTimeMillis()));
		
		//FIXME  Secret ID should be Machine Specific, Persist along with machine
		String secretKey = getMachineSecreteKey(machineId);
		String machineKey = getMachineEncryptionKey(machineId);
		
		AES.setKey(secretKey);
		return AES.encrypt(builder.toString(), machineKey);
		
	}

	private String getMachineSecreteKey(String machineId) {
		// FIXME Fetch encryption Key for Machine 
		// Query query = new Query()
				return machineId+"Champ";
	}

	private String getMachineEncryptionKey(String machineId) {
		// FIXME Fetch encryption Key for Machine 
		// Query query = new Query()
		
		return machineId;
		
		
	}

	private PrintCostPaymentRequest getPrintRequestDetailsByPrintJobId(String printjobId) {
       Query query = new Query(Criteria.where("printJobId").is(printjobId));
       PrintCostPaymentRequest printCostPaymentRequest = mtemplate.findOne(query, PrintCostPaymentRequest.class);
       return printCostPaymentRequest;
       //
	}

	private String getMachineId(String ssid) {
		// TODO Auto-generated method stub
		return "8782";
	}
 
	@Override
	public BulkPrintOrder placeOrder(BulkPrintOrder bulkOrder) {
		
		if (bulkOrder.getOrderId() == null) {
			throw new NoRecordFoundException("Invalid Request. incorrect Order");
		}
		// CheckAddress
		if(bulkOrder.getDeliveryAddress() == null || bulkOrder.getDeliveryAddress().getCityName() == null || bulkOrder.getDeliveryAddress().getPincode()== null)
		{
			throw new InvalidDataException("Address can not be null. City Name and Pincode is mendatory");
		}
		// To-Do
		// validate the Request with OrderID Might be It is tempered by user
		// Verify Payment 
		// Verify order with Caching
		
		bulkOrder = updateBulkOrderDetails(bulkOrder);
		
		return bulkOrder;
	}

	private BulkPrintOrder updateBulkOrderDetails(BulkPrintOrder bulkOrder) {
		
		 Query query = new Query(Criteria.where("orderId").is(bulkOrder.getOrderId()).and("orderStaus").is("CHECKOUT"));
		
		BulkPrintOrder bulkOrder1 = mtemplate.findOne(query, BulkPrintOrder.class); 
				//and(Criteria.where("orderStaus").is("CHECKOUT")));
		Update update = new Update();
		update.set("deliveryAddress", bulkOrder.getDeliveryAddress());
		update.set("paymentStatus", PaymentStatus.COMPLETED.name());
		update.set("orderStaus", "ACCEPTED");
		update.set("createDate", System.currentTimeMillis());
		
		
		UpdateResult result = mtemplate.updateFirst(query, update, BulkPrintOrder.class);
		logger.info("=========Update =={} === {}", result.isModifiedCountAvailable(), result.getModifiedCount());
		if(result.getModifiedCount() == 0) {
			throw new InvalidBulkPrintException("Order Already Placed or Invalid Order");
		}
		try{
		paymentService.executePaymentForBulk(bulkOrder1);
		
		bulkOrder1.setDeliveryAddress(bulkOrder.getDeliveryAddress());
		bulkOrder1.setPaymentStatus(PaymentStatus.COMPLETED.name());
		bulkOrder1.setOrderStaus("ACCEPTED");
		bulkOrder1.setSufficientAmount(true);
		return bulkOrder1;
		}catch (Exception e) {
			logger.error(" Failed to Place Order ", e);
		}
		// update payment
		
		return null;
	}

	@Override
	public BulkPrintOrder orderDetailsByOrderId(String orderId) {
		Query query = new Query(Criteria.where("orderId").is(orderId));
		return mtemplate.findOne(query, BulkPrintOrder.class);
	}

	@Override
	public List<BulkPrintOrder> listOfPendingOrders(String emailId) {
		
		Query query = new Query(Criteria.where("emailId").is(emailId));
		return mtemplate.find(query, BulkPrintOrder.class);
		
	}

	@Override
	public Factor2OTPResponse generateOTP(String mobileNo) {
		Query query = new Query(Criteria.where("mobile").is(mobileNo));
		OTPRecord record = mtemplate.findOne(query, OTPRecord.class);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		if(record == null) {
			//generate OTP
			record = new OTPRecord();
			record.setMobile(mobileNo);
			record.setCreateOn(timestamp);
			Factor2OTPResponse factor2OTPResponse = smsSendService.sendOTPForActivateAccount(mobileNo);
			mtemplate.save(record);
			// call OTP
			return factor2OTPResponse;
			
			
		}else {
		   throw new OTPAuthenticationException("Use Existing OTP");
		}
		
		
	}

	@Override
	public void updateMachineIP(String machineId, String ipaddress) {
		
		Query query = new Query(Criteria.where("machineId").is(machineId));
		Update update = new Update();
		update.set("wifiIPAdrress", ipaddress);
		mtemplate.updateFirst(query, update, WIFIMachine.class);
		
	}
	
	

}
