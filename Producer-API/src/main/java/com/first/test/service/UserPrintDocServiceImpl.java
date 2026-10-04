package com.first.test.service;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator.RequestorType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.profile.path.config.SharedConfigDefaultLocationProvider;
import com.amazonaws.services.appstream.model.ResourceNotAvailableException;
import com.first.test.aws.S3BucketAccess;
import com.first.test.config.ProducerKafkaConfig;
import com.first.test.config.ServiceConfig;
import com.first.test.doa.CustomerPrintDao;
import com.first.test.doa.MachineDao;
import com.first.test.exception.DataValidationException;
import com.first.test.exception.IncompletePaymentException;
import com.first.test.exception.InvalidDataException;
import com.first.test.exception.NoRecordFoundException;
import com.first.test.exception.PaymentFailureException;
import com.first.test.exception.PrintRequestValidationException;
import com.first.test.model.BusinessType;
import com.first.test.model.CostDerivative;
import com.first.test.model.Customer;
import com.first.test.model.MachineDetails;
import com.first.test.model.PartnerItemRequest;
import com.first.test.model.PaymentDetailsForMongoDB;
import com.first.test.model.PrintCostPaymentRequest;
import com.first.test.model.PrintDocRequest;
import com.first.test.model.PrintJobStatusUpdate;
import com.first.test.model.PrintProperties;
import com.first.test.model.PrintRequestType;
import com.first.test.model.ShareDocForMachineByProfessionals;
import com.first.test.model.SharedDocForUser;
import com.first.test.model.ThirdPartyPaymentDetails;
import com.first.test.model.WalletModel;
import com.first.test.model.WhoSendPrintCommand;
import com.first.test.producer.ZeroxProducer;
import com.first.test.util.ErrorCodeNo;
import com.first.test.util.PaymentStatus;
import com.first.test.util.PrintStatus;
import com.first.test.util.RefundStatus;
import com.first.test.util.RequestType;
import com.first.test.util.TransConstants.TransactionStatus;
import com.first.test.util.TransType;

import rx.internal.util.LinkedArrayList;

@Service
public class UserPrintDocServiceImpl implements UserPrintDocService {

	private static Logger logger = LoggerFactory.getLogger(UserPrintDocServiceImpl.class);
	/*
	 * @Autowired AWSS3Services multipartFile;
	 */
	@Value("${env}")
	private String env;

	@Autowired
	ZeroxProducer zeroxProducer;

	@Autowired
	CustomerPrintDao customerPrintDao;

	@Autowired
	S3BucketAccess s3BucketAccess;

	@Autowired
	MachineDao machineDao;

	@Autowired
	PaymentService paymentService;

	@Autowired
	CustomerProfileService customerProfileService;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ProducerKafkaConfig producerKafkaConfig;
	
	@Autowired
	ServiceConfig serviceConfig;


	@Override
	public PrintDocRequest persistPrintRequest(String machineId, PrintDocRequest printDocRequest, MultipartFile file) {

		// Set TimeStamp
		printDocRequest.setCreateOn(System.currentTimeMillis());
		
		// Validate Request when Cost, wallet balance , page , expression etc before Processing 
				// Make DB call ad check 
		  if(!validatePrintRequestAgainstPaymentandPageCount(printDocRequest)) {
			  logger.error(" Request Tempered ==========");
			  throw new PrintRequestValidationException("Request Tempered and mismatch payment and Request", ErrorCodeNo.TemperedReuqest.getValue());
		  }
		  
		  printDocRequest = paymentService.executePaymentOfJob(printDocRequest);
		  
		  logger.info("========= " + printDocRequest.toString()); // generate S3 Bucket
		  if(printDocRequest.getPaymentStatus() == null) {
			  printDocRequest.setPaymentStatus(PaymentStatus.PENDING.name());
		  }
		 
		// Case for Remote Share and Direct Share
		if (file != null) {
			String s3path = savefileInS3Bucket(printDocRequest.getMachineId(), printDocRequest.getEmailId(), file,
					printDocRequest.getPrintJobId());
			printDocRequest.setDocumentURL(s3path);
		}

		// persist the print request in DB, payment status
		// case paymentStatus = completed go ahead and print
		// case paymentStatus = partial. persist doc details, response containing Doc ID
		// and balance amount from other payment gateway
		// case PaymentStatus = Pending. Persist doc Details, response containing Doc ID
		// and amount for payment gateway
		// On successful payment pass all those ids belong to payment and

		// Persist Request in DB
		logger.info("========= Persiting Doc Request ==========="+printDocRequest.toString());
		customerPrintDao.persist(printDocRequest);

		String partnerId = checkMachineIsPartnerMachine(printDocRequest.getMachineId());
		// If payment not comleted and machine is not partner machine
		logger.info("=== Partner ID ===========" +partnerId + "\n\n"+ printDocRequest.toString() );
		if (!printDocRequest.getPaymentStatus().equals("COMPLETED") && partnerId != null) {
			// case paymentStatus = partial. persist doc details, response containing Doc ID
			// and balance amount from other payment gateway
			// case PaymentStatus = Pending. Persist doc Details, response containing Doc ID
			// and amount for payment gateway
			return printDocRequest;
		}

		if (partnerId != null) {
			printDocRequest.setRequestType(PrintRequestType.DIRECT.name());
			printDocRequest.setBusinessType(BusinessType.PARTNER.name());
		} else if (partnerId == null && printDocRequest.getRequestType() == null) {
			printDocRequest.setRequestType(PrintRequestType.DIRECT.name());
			printDocRequest.setBusinessType(BusinessType.ZEROX.name());
		} else if (partnerId == null && printDocRequest.getBusinessType() == null) {
			printDocRequest.setRequestType(PrintRequestType.DIRECT.name());
			printDocRequest.setBusinessType(BusinessType.ZEROX.name());
		}

		// generate S3 Bucket Path

		// multipartFile.persistFile(printDocRequest.getMachineId(),
		// printDocRequest.getMobileNo(), securityNumber, file);

		try {

			// publish details to topic

			PrintDocRequest publishForMachine = printDocRequest;
			// payment Details
			// publishForMachine.setPaymentDetails(null);
			publishForMachine.setBalanceAmountToPay(0.0f);
			// publishForMachine.setThirdPartyPaymentDetails(null);
			publishForMachine.setPrintStatus(PrintStatus.PENDING.name());

			logger.info("Ready To Publish" + printDocRequest.toString());

			// FIXME uncomment Kafka Publish
			// zeroxProducer.runProducer(printDocRequest.getMachineId(), publishForMachine);
			// Persit in MongoDB
			// mongoTemplate.save(printDocRequest);
            // Deducted Amount from wallet
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(" Failed to Pring Doc Direct ", e);
			// To-DO Failure mechanism

		}
		return printDocRequest;

	}

	private boolean validatePrintRequestAgainstPaymentandPageCount(PrintDocRequest printDocRequest) {

		try {
			PrintDocRequest fetchPrintDocRequest =	fetchPrintJobRequestByPrintJobId(printDocRequest.getPrintJobId(), printDocRequest.getEmailId());
			
			logger.info(" This Fetch   {} \n and given :  {}", fetchPrintDocRequest.toString(), printDocRequest.toString());
			
			if(fetchPrintDocRequest == null) {
				new DataValidationException("PrintJobId is not Exist", ErrorCodeNo.TemperedReuqest.getValue());
			}
			logger.info(" This EmailId  {} \n {}", fetchPrintDocRequest.getEmailId(), fetchPrintDocRequest.toString());
			if(! fetchPrintDocRequest.getEmailId().equals(printDocRequest.getEmailId())) {
				logger.info("EAMILID");
				return false;
			}
			
			//FIXME Very Important Condition for Avoid Hacking and Tempering
			/*if(!fetchPrintDocRequest.getPrintProperties().getPexpn().equals(printDocRequest.getPrintProperties().getPexpn())){
				return false;
			}*/
			
			if(fetchPrintDocRequest.getPrintProperties().getPageCount() != printDocRequest.getPrintProperties().getPageCount()){
				logger.info("== Page Count ");
				
				return false;
			}
			
			/*if(fetchPrintDocRequest.getPrintCost() != printDocRequest.getPrintCost()) {
				logger.info("   Cost ==");
				return false;
			}*/
		}catch(Exception e) {
			logger.error(" Failed to Validate :", e);
			throw new DataValidationException("Payment and Request Tempared", ErrorCodeNo.TemperedReuqest.getValue());
		}
		return true;
		
	}

	private PrintDocRequest fetchPrintJobRequestByPrintJobId(String printJobId, String emailId) {
		
		// Read from Redis If data not present in Redis make call to DB
		
		Query query = new Query(Criteria.where("printJobId").is(printJobId));
		PrintCostPaymentRequest printCostPaymentRequest = mongoTemplate.findOne(query, PrintCostPaymentRequest.class);
		if( printCostPaymentRequest == null) {
			logger.error(" PrintJOb does not exist ");
			throw new DataValidationException("PrintJObId does not exist or already Used ", ErrorCodeNo.TemperedReuqest.getValue());
			
		}
		PrintDocRequest printDocRequest = new PrintDocRequest();
		
		try {
		logger.info(" Printrequest : {}", printCostPaymentRequest);
		printDocRequest.setPrintJobId(printCostPaymentRequest.getPrintJobId());
		printDocRequest.setPrintCost(printCostPaymentRequest.getTotalCost());
		printDocRequest.setEmailId(printCostPaymentRequest.getEmailId());
		PrintProperties printProp = new PrintProperties();
		
		printProp.setPageCount(printCostPaymentRequest.getNumberOfPage());
		printProp.setPexpn(printCostPaymentRequest.getPrintExprn());
		printProp.setColor(printCostPaymentRequest.isColor());
		
		printDocRequest.setPrintProperties(printProp);
		logger.info(" Printrequest Mapped : {}", printDocRequest);
		return printDocRequest;
		}catch (Exception e) {
			logger.error(" Failed to process print request  ", e);
		}
		return printDocRequest;
	}

	private String checkMachineIsPartnerMachine(String machineId) {
		Query query = new Query(Criteria.where("machineId").is(machineId));
		MachineDetails machineDetails = mongoTemplate.findOne(query, MachineDetails.class);
		if (machineDetails != null) {
			logger.info(" Machine ID {}", machineDetails.toString() );
			return machineDetails.getPartnerId();
		}
		return null;
	}

	// printDocRequest.getMachineId(), printDocRequest.getMobileNo(),securityNumber,
	// file
	// printDocRequest.setDocumentURL(s3path);
	private String savefileInS3Bucket(String machineId, String emailId, MultipartFile file, String printjobId) {
  
		String s3path = s3BucketAccess.persistFile(machineId, emailId, printjobId, file);
		logger.debug("S3 Path :" + s3path);
		return s3path;
	}

	private String createSecurityNumber() {
		// TODO Auto-generated method stub
		return "test";
	}

	@Override
	public CostDerivative getPrintCost(String machineId) {
		// TODO Auto-generated method stub
		return null;//customerPrintDao.getCostDerivativeByMachineId(machineId, null);
	}

	@Override
	@Transactional
	public void updatePrintJobstatus(PrintJobStatusUpdate printJobStatusUpdate) {
		// initiate refund to wallet
		if(printJobStatusUpdate.getPrintJobStatus().equals(PrintStatus.FAIL.name()) && printJobStatusUpdate.getRefundInitiate().equals(RefundStatus.INIT.name())) {
			refundAndStatusUpdate(printJobStatusUpdate);		
		}else if(printJobStatusUpdate.getPrintJobStatus().equals(PrintStatus.COMPLETE.name())){
			PrintDocRequest printDocRequest = getPrintJobDetails(printJobStatusUpdate.getMachineId(), printJobStatusUpdate.getPrintJobId());
			paymentService.finalPaymentDeductedFromMachine(printDocRequest);
		}
		
		// update Print job Status
		customerPrintDao.updatePrintJobstatus(printJobStatusUpdate);
		// Failure analysis and notify User

		// update the Caretaker for machine issue paper for notification
		machineDao.updatePaperMachinePaperInkCountforRefile(printJobStatusUpdate);
		

	}

	

	private void refundAndStatusUpdate(PrintJobStatusUpdate printJobStatusUpdate) {
		try {
		//FIXME Publish in Kafka for Refund with KEY=REFUND
		// Add amoun in wallet 
		Query query = new Query(Criteria.where("orderId").is(printJobStatusUpdate.getPrintJobId()));
		
		PaymentDetailsForMongoDB paymentDetails = mongoTemplate.findOne(query, PaymentDetailsForMongoDB.class);
		if(paymentDetails.getTransType().equals(TransType.DEBIT.name())) {
		  float amount = paymentDetails.getAmount();
		  PaymentDetailsForMongoDB credtAmount = new PaymentDetailsForMongoDB();
		  
		  paymentDetails.setAmount(amount);
		  paymentDetails.setCreateDate(System.currentTimeMillis());
		  String newOrderId = "ref_"+paymentDetails.getOrderId();
		  paymentDetails.setOrderId(newOrderId);
		  paymentDetails.setTransId(newOrderId);
		  paymentDetails.setTransStatus(TransactionStatus.refunded.name());
		  paymentDetails.setAmount(amount);
		  paymentDetails.setTransType(TransType.CREDIT.name());
		  
		  Query query2 = new Query(Criteria.where("wid").is(paymentDetails.getWid()));
		  WalletModel walletModel = mongoTemplate.findOne(query2, WalletModel.class);
		  
		  Update update = new Update();
		  update.set("balance", walletModel.getBalance()+amount);
		  // refund to wallet
		  mongoTemplate.updateFirst(query2, update, WalletModel.class);
		  // create payments
		  mongoTemplate.save(paymentDetails);
		  
		}
		}catch (Exception e) {
			// FIXME
			// Retry and rollback machenism
			logger.error("  Failed o refund ", e);
		}
	}

	// Share doc for print across the group
	@Override
	public void shareprintdoc(String machineId, String mobileNolist, String mobileOfOwnerOfdoc, MultipartFile file) {
		// persist details

		// create print job for each mobile and persist in DB.
		// Think for pending Tableas well ?
		// if machineID unknown. machineID = "",
		// should not publish
		// else publish to machine
		//

	}

	

	@Override
	public PrintDocRequest printPaymentPendingRequest(String machineId, PrintDocRequest printRequest) {
		// TODO Auto-generated method stub
		// check payment status
		// get the DocPrintRequest from
		System.out.println("Pending payment doc :" + printRequest.getPrintJobId());
		PrintDocRequest pandingPaymentDoc = getPrintRequestForPendingPayment(printRequest.getPrintJobId());
		if (pandingPaymentDoc == null) {
			throw new InvalidDataException("Data Manipulated and Invalid");
		}

		if (!pandingPaymentDoc.getMobileNo().equalsIgnoreCase(printRequest.getMobileNo())) {
			throw new InvalidDataException("Data Manipulated and Invalid");
		}

		// printRequest.getThirdPartyPaymentDetails().getPaymentResponse();
		String paymentstatus = "TXN_SUCCESS";// getPaymentStatusFromJSON(
		// printRequest.getThirdPartyPaymentDetails().getPaymentResponse());
		if (!paymentstatus.equals("TXN_SUCCESS")) {
			throw new PaymentFailureException("Unable to proceed with request");
		}

		// validate payment
		// validatePaymentForPrintRequest(printRequest.getThirdPartyPaymentDetails());
		// validate pending amount and transaction is ok
		// extract payment amount and populate in Printrequest
		double transactionAmount = 100; // extractTransactionAmount(printRequest.getThirdPartyPaymentDetails());

		if (transactionAmount >= printRequest.getBalanceAmountToPay()) {
			logger.info("Payment Completed");
			printRequest.setPaymentStatus("COMPLETE");
			printRequest.setBalanceAmountToPay(0.0f);
		} else {
			// deduct from
			printRequest.setBalanceAmountToPay(printRequest.getBalanceAmountToPay() - (float) transactionAmount);
			return printRequest;
		}
		// persist doc
		logger.info(" Update Doc Details");
		customerPrintDao.upsertPrintDocRequestAfterPayment(printRequest);
		// publish doc
		try {
			logger.info("Ready to publish Doc");
			zeroxProducer.runProducer(printRequest.getMachineId(), printRequest);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return printRequest;
	}

	// Get the details of Print request for payment pending
	private PrintDocRequest getPrintRequestForPendingPayment(String docId) {
		Query query = new Query(Criteria.where("docId").is(docId));
		PrintDocRequest printDocRequest = mongoTemplate.findOne(query, PrintDocRequest.class);
		System.out.println("Query =========== " + query.getQueryObject().toString());
		return printDocRequest;

	}

	private double extractTransactionAmount(ThirdPartyPaymentDetails thirdPartyPaymentDetails) {
		// TODO Auto-generated method stub
		return 100;
	}

	private void validatePaymentForPrintRequest(ThirdPartyPaymentDetails thirdPartyPaymentDetails) {
		// TODO Auto-generated method stub

	}

	private void validatePaymentForPrintRequest() {
		// TODO Auto-generated method stub
		// Step 1: Pending amount and made payment amount is equal or greater
		// Step 2: check Benifeciary account is ZeroxVM account
		// Step 3: Persite Payment details in DB

	}

	private String getPaymentStatusFromJSON(String paymentResponse) {
		// TODO Auto-generated method stub
		return "TXN_SUCCESS";
	}

	// When User Send request from Remotely on One System and later he want print
	// doc by own self or Guy who autherize to take print
	@Override
	public void publishPrintCommandByOwnerOfDoc(PrintDocRequest printDocRequest) {
		// MobileNo and Code both lying on Partner DashBoard
		// when mobile and Security validate at partner then Publish

		String partnerId = checkMachineIsPartnerMachine(printDocRequest.getMachineId());

		// To-Do
		// check from DB payment is completed of not.
		PrintDocRequest printDocRequestFromDB = getPrintRequestFromDB(printDocRequest.getPrintJobId());
		if (!printDocRequestFromDB.getPaymentStatus().equals(PaymentStatus.COMPLETED.name())) {
			logger.error("Print Request failed to process ");
			throw new IncompletePaymentException("Unable to process request");
		}
		try {
			// REMOTE VS PRINT_LATER.
			// REMOTE: means Just persit request on Remote System
			// PRINT_LATER : Print Command when User or Autherise to print
			logger.info("is Machine PartnerMachine " + partnerId);
			if (printDocRequest.getRequestType() == "PRINT_LATER" || partnerId == null) {
				logger.info("Seting Business type");
				printDocRequest.setBusinessType(BusinessType.ZEROX.name());
			}
			logger.info(" ========= " + printDocRequest.toString());
			if (printDocRequest.getRequestType().equals("PRINT_LATER")
					&& printDocRequest.getBusinessType() == BusinessType.ZEROX.name()) {
				logger.info("Reay to PMap and Publkish ");
				PartnerItemRequest partnerItemRequest = new PartnerItemRequest();
				partnerItemRequest.setDocId(printDocRequest.getPrintJobId());
				partnerItemRequest.setMachineId(printDocRequest.getMachineId());
				partnerItemRequest.setMobileNo(printDocRequest.getMobileNo());
				partnerItemRequest.setPartnerId(printDocRequest.getCustId());
				partnerItemRequest.setRequestType(RequestType.PRINT_LATER.name());
				logger.info("   Partner Print :" + partnerItemRequest.toString());

				// zeroxProducer.runProducerCustomeTopic(printDocRequest.getMachineId(),
				// printDocRequest, producerKafkaConfig.getPartnerTopic());

				zeroxProducer.runProducer(partnerItemRequest.getMachineId(), partnerItemRequest);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private PrintDocRequest getPrintRequestFromDB(String docId) {
		// TODO Auto-generated method stub
		Query query = new Query(new Criteria().where("docId").is(docId));

		return mongoTemplate.findOne(query, PrintDocRequest.class);
	}

	// Pending request of User and to whom autherize
	@Override
	public List<PrintDocRequest> getPendingPrintRequests(String emailId, String mobileId) {

		// FIXME EmailID based QUERY
		Query query = new Query(new Criteria().where("sharedForMobile").is(mobileId)
				.andOperator(Criteria.where("printStatus").is(PrintStatus.PENDING.name())));
		logger.info("Query :" + query.getQueryObject());
		List<PrintDocRequest> pendingDocList = mongoTemplate.find(query, PrintDocRequest.class);
		return pendingDocList;
	}

	@Override
	public PrintDocRequest printsharedDoc(PrintDocRequest printDocRequest) {

		// Create Security number
		String securityNumber = createSecurityNumber();
		printDocRequest.setSecurityCode(securityNumber);
		String customerId = customerPrintDao.getCustomerIdByPhoneAndEmail(printDocRequest.getMobileNo());
		printDocRequest.setCustId(customerId);
		printDocRequest = paymentService.executePaymentOfJob(printDocRequest);

		logger.info("========= " + printDocRequest.toString());

		// persist the print request in DB, payment status
		// case paymentStatus = completed go ahead and print
		// case paymentStatus = partial. persist doc details, response containing Doc ID
		// and balance amount from other payment gateway
		// case PaymentStatus = Pending. Persist doc Details, response containing Doc ID
		// and amount for payment gateway
		// On successful payment pass all those ids belong to payment and

		// Persist Request in DB
		logger.info("========= Persiting Doc Request ===========");
		customerPrintDao.persist(printDocRequest);

		String partnerId = checkMachineIsPartnerMachine(printDocRequest.getMachineId());
		// If payment not comleted and machine is not partner machine
		if (!printDocRequest.getPaymentStatus().equals("COMPLETED") && partnerId != null) {
			// case paymentStatus = partial. persist doc details, response containing Doc ID
			// and balance amount from other payment gateway
			// case PaymentStatus = Pending. Persist doc Details, response containing Doc ID
			// and amount for payment gateway
			return printDocRequest;
		}

		if (partnerId != null) {
			printDocRequest.setRequestType(PrintRequestType.DIRECT.name());
			printDocRequest.setBusinessType(BusinessType.PARTNER.name());
		} else if (partnerId == null && printDocRequest.getRequestType() == null) {
			printDocRequest.setRequestType(PrintRequestType.DIRECT.name());
			printDocRequest.setBusinessType(BusinessType.ZEROX.name());
		} else if (partnerId == null && printDocRequest.getBusinessType() == null) {
			printDocRequest.setRequestType(PrintRequestType.DIRECT.name());
			printDocRequest.setBusinessType(BusinessType.ZEROX.name());
		}

		// generate S3 Bucket Path

		// multipartFile.persistFile(printDocRequest.getMachineId(),
		// printDocRequest.getMobileNo(), securityNumber, file);

		try {

			// publish details to topic

			PrintDocRequest publishForMachine = printDocRequest;
			// Payment details update
			// publishForMachine.setPaymentDetails(null);
			publishForMachine.setBalanceAmountToPay(0.0f);
			// publishForMachine.setThirdPartyPaymentDetails(null);
			publishForMachine.setPrintStatus(PrintStatus.PENDING.name());

			logger.info("Ready To Publish" + printDocRequest.toString());

			zeroxProducer.runProducer(printDocRequest.getMachineId(), publishForMachine);
			// Persit in MongoDB

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return printDocRequest;

	}

	@Override
	public String getUploadS3URL(String printJobId, String emailId) {
		// TODO Auto-generated method stub
		return null;//s3BucketAccess.getS3BucketURL(printJobId).toString();
	}
	

	@Override
	public List<PrintDocRequest> printJobHistory(String emailId, String status, String requestType) {
		Set<PrintDocRequest> resultList = new HashSet();
		try {
		Query query =  new Query();
		/*		if (requestType.equalsIgnoreCase(RequestType.REMOTE.name())
					|| status.equalsIgnoreCase(PrintStatus.PENDING.name())) {
				
				query.addCriteria(
					    new Criteria().andOperator(
					    		Criteria.where("emailId").is(emailId),
					    		Criteria.where("requestType").is(RequestType.REMOTE.name()),
					    		Criteria.where("printStatus").is(PrintStatus.PENDING.name())
					    )
					);
			} else {
				query = new Query(Criteria.where("emailId").is(emailId));
			}
			
			
			*/
			
			Criteria emailCrit = new Criteria();
			if (status == null || requestType == null) {
				query.addCriteria(Criteria.where("emailId").is(emailId));
			} else {

				if (requestType.equalsIgnoreCase(RequestType.REMOTE.name())
						&& status.equalsIgnoreCase(PrintStatus.PENDING.name())) {

					query.addCriteria(new Criteria().andOperator(
							new Criteria().orOperator(Criteria.where("emailId").is(emailId), Criteria.where("sharedForEmail").is(emailId)),
							Criteria.where("requestType").is(RequestType.REMOTE.name()),
							Criteria.where("printStatus").is(PrintStatus.PENDING.name())));
				}
			}

			query.fields().include("emailId").include("printCost").include("docName").include("orderId")
					.include("printErrorCode").include("printProperties.pageCount").include("printStatus")
					.include("requestType").include("sharedForEmail").include("createOn"); //
			query.limit(serviceConfig.getRecordLimit());
			query.with(new Sort(Sort.Direction.DESC, "createOn"));

			List<PrintDocRequest> list = mongoTemplate.find(query, PrintDocRequest.class);
			resultList.addAll(list);
			System.out.println("======================== "+ resultList.toString());
			return new ArrayList<>(resultList);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Print Job History Failed ", e);

		}
		return new LinkedList<>();

	}

	@Override
	public PrintDocRequest remotePrintTrigger(String printjobId, String machineId) {
		PrintDocRequest request = null;
		try {
		Query query = new Query(Criteria.where("printJobId").is(printjobId));
		query.fields().include("printJobId").include("printStatus");
		 request = mongoTemplate.findOne(query, PrintDocRequest.class);
		 
		if(request == null) {
			throw new PrintRequestValidationException("Invalid Print ID", 209);
		}
		if(!request.getPrintStatus().equalsIgnoreCase(PrintStatus.PENDING.name())) {
			throw new PrintRequestValidationException("Invalid Print Request", 209);
		}
		if(!request.getPaymentStatus().equalsIgnoreCase(PaymentStatus.COMPLETED.name())) {
			throw new PrintRequestValidationException("Payment Incomplete", 2010);
		}
		// FIXME
		// Compare emailID and Request EMAIL ID 
		
		// update the status in DB from Pending to Processing
		Update update = new Update();
		update.set("printStatus", PrintStatus.PROCESSING.name());
		mongoTemplate.updateFirst(query, update, PrintDocRequest.class);
		
		// FIXME
		// Publish in MQTT
		// zeroxProducer.runProducer(machineId, request);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return request;
	}

	@Override
	public PrintDocRequest persistRemotePrintRequest(String machineId, PrintDocRequest printDocRequest,
			MultipartFile file) {
		// Set TimeStamp
				printDocRequest.setCreateOn(System.currentTimeMillis());
				// Create Security number
				 
				// Validate Request when Cost, wallet balance , page , expression etc before Processing 
						// Make DB call ad check 
				  if(!validatePrintRequestAgainstPaymentandPageCount(printDocRequest)) {
					  logger.error(" Request Tempered ==========");
					  throw new PrintRequestValidationException("Request Tempered and mismatch payment and Request", ErrorCodeNo.TemperedReuqest.getValue());
				  }
				  printDocRequest = paymentService.executePaymentOfJob(printDocRequest);
				  
				  logger.info("========= " + printDocRequest.toString()); // generate S3 Bucket
				  if(printDocRequest.getPaymentStatus() == null) {
					  printDocRequest.setPaymentStatus(PaymentStatus.PENDING.name());
				  }
				 
				// Case for Remote Share and Direct Share
				if (file != null) {
					String s3path = savefileInS3Bucket(printDocRequest.getMachineId(), printDocRequest.getEmailId(), file,
							printDocRequest.getPrintJobId());
					printDocRequest.setDocumentURL(s3path);
				}

				// persist the print request in DB, payment status
				// case paymentStatus = completed go ahead and print
				// case paymentStatus = partial. persist doc details, response containing Doc ID
				// and balance amount from other payment gateway
				// case PaymentStatus = Pending. Persist doc Details, response containing Doc ID
				// and amount for payment gateway
				// On successful payment pass all those ids belong to payment and

				// Persist Request in DB
				
				String partnerId = checkMachineIsPartnerMachine(printDocRequest.getMachineId());
				// If payment not comleted and machine is not partner machine
				logger.info("=== Partner ID ===========" +partnerId + "\n\n"+ printDocRequest.toString() );
				if (!printDocRequest.getPaymentStatus().equals("COMPLETED") && partnerId != null) {
					// case paymentStatus = partial. persist doc details, response containing Doc ID
					// and balance amount from other payment gateway
					// case PaymentStatus = Pending. Persist doc Details, response containing Doc ID
					// and amount for payment gateway
					return printDocRequest;
				}
				printDocRequest.setRequestType(PrintRequestType.REMOTE.name());

				if (partnerId != null) {
					printDocRequest.setRequestType(PrintRequestType.REMOTE.name());
					printDocRequest.setBusinessType(BusinessType.PARTNER.name());
				} else if (partnerId == null && printDocRequest.getRequestType() == null) {
					printDocRequest.setRequestType(PrintRequestType.REMOTE.name());
					printDocRequest.setBusinessType(BusinessType.ZEROX.name());
				} else if (partnerId == null && printDocRequest.getBusinessType() == null) {
					printDocRequest.setRequestType(PrintRequestType.REMOTE.name());
					printDocRequest.setBusinessType(BusinessType.ZEROX.name());
				}

				logger.info("========= Persiting Doc Request ==========="+printDocRequest.toString());
				customerPrintDao.persist(printDocRequest);

				// generate S3 Bucket Path

				// multipartFile.persistFile(printDocRequest.getMachineId(),
				// printDocRequest.getMobileNo(), securityNumber, file);

				try {

					// publish details to topic

					PrintDocRequest publishForMachine = printDocRequest;
					// payment Details
					// publishForMachine.setPaymentDetails(null);
					publishForMachine.setBalanceAmountToPay(0.0f);
					// publishForMachine.setThirdPartyPaymentDetails(null);
					publishForMachine.setPrintStatus(PrintStatus.PENDING.name());

					logger.info("Ready To Publish" + printDocRequest.toString());
					// FIXME Remove comments for Kafka Publish
					// zeroxProducer.runProducer(printDocRequest.getMachineId(), publishForMachine);
					// Persit in MongoDB
					// mongoTemplate.save(printDocRequest);
		            // Deducted Amount from wallet
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error(" Failed to Pring Doc Direct ", e);
					// To-DO Failure mechanism

				}
				return printDocRequest;

	}

	@Override
	public PrintDocRequest getPrintJobDetails(String machineId, String printJobId) {
		Query query = new Query(Criteria.where("printJobId").is(printJobId));
	
		PrintDocRequest printDocRequest = mongoTemplate.findOne(query, PrintDocRequest.class);
		if(printDocRequest == null) {
			throw new NoRecordFoundException("Invalid PrintJobId", ErrorCodeNo.NoRecord.getValue());
		}
		if(printDocRequest != null && ! printDocRequest.getPrintStatus().equals(PrintStatus.PENDING.name())) {
			throw new PrintRequestValidationException("Invalid PrintJobId", ErrorCodeNo.TemperedReuqest.getValue());
		}
		/*if(!machineId.equals(printDocRequest.getMachineId())) {
			throw new PrintRequestValidationException("Invalid PrintJobId", ErrorCodeNo.TemperedReuqest.getValue());
		}*/
		return printDocRequest;
	}
}
