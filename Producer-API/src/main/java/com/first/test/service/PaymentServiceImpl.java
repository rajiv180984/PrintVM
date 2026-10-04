package com.first.test.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first.test.config.ServiceConfig;
import com.first.test.encrypt.AESUtils;
import com.first.test.exception.DataValidationException;
import com.first.test.exception.InvalidDataException;
import com.first.test.exception.NoRecordFoundException;
import com.first.test.exception.PaymentFailureException;
import com.first.test.exception.PrintRequestValidationException;
import com.first.test.model.BulkPrintOrder;
import com.first.test.model.Customer;
import com.first.test.model.JobCostAndPaymentPojo;
import com.first.test.model.OrderDetails;
import com.first.test.model.PaymentDetails;
import com.first.test.model.PaymentDetailsForMongoDB;
import com.first.test.model.PrintCostPaymentRequest;
import com.first.test.model.PrintDocRequest;
import com.first.test.model.ThirdPartyPaymentDetails;
import com.first.test.model.WalletModel;
import com.first.test.model.ZeroxPaymentDetails;
import com.first.test.util.CommonUtility;
import com.first.test.util.ErrorCodeNo;
import com.first.test.util.PaymentStatus;
import com.first.test.util.PrintStatus;
import com.first.test.util.ServiceStatus;
import com.first.test.util.TransConstants;
import com.first.test.util.TransType;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Autowired
	CustomerProfileService customerService;

	@Autowired
	OfferService offerService;

	@Autowired
	MongoTemplate mtemplate;
	
	@Autowired
	ServiceConfig serviceConfig;
	
	@Autowired
	AESUtils aesUtils;

	@Override
	public String evaluateCostOfJob(String machineId, String userId, int numberOfPage, String copounCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrintDocRequest executePaymentOfJob(PrintDocRequest printDocRequest) {

		// To-DO Tax Calculation

		// Validate the Coupons and Get Coupon amount;
		float couponValue = offerService.getValidatePromocodeAndValue(printDocRequest.getEmailId(),
				printDocRequest.getPromocode());
		
		
		float printCost = (float) printDocRequest.getPrintCost();
		printCost = printCost - couponValue;
		printDocRequest.setPrintCost(printCost);
		// update Wallet by email Id
		printDocRequest = updateWalletDetail(printDocRequest);
		
		// FIXME If Partner Printer for Shared Printer

		return printDocRequest;

	}

	public PrintDocRequest calculateAmountForPrintJob(PrintDocRequest printDocRequest) {

		// To-DO Tax Calculation

		// Validate the Coupons and Get Coupon amount;
		float couponValue = offerService.getValidatePromocodeAndValue(printDocRequest.getEmailId(),
				printDocRequest.getPromocode());
		logger.info(
				"== Offer Process ===========  Coupon " + printDocRequest.getPromocode() + " and Value " + couponValue);
		float printCost = (float) printDocRequest.getPrintCost();
		printCost = printCost - couponValue;
		printDocRequest.setPrintCost(printCost);
		// update Wallet by email Id
		printDocRequest = updateWalletDetail(printDocRequest);

		printDocRequest.setPaymentStatus("COMPLETED");

		return printDocRequest;
	}

	private ZeroxPaymentDetails getWalletDetailOfUser(String mobileNo, String custId) {

		logger.info("========= Mobile No:" + mobileNo + " ======== " + custId);
		Query query = new Query(new Criteria("mobileNo").is(mobileNo).andOperator(new Criteria("custId").is(custId)));
		ZeroxPaymentDetails zeroxPaymentDetails = mtemplate.findOne(query, ZeroxPaymentDetails.class);
		return zeroxPaymentDetails;
	}

	@Override
	public String mockPaymentAPI(ThirdPartyPaymentDetails thirdPartyPaymentDetails) {
		ZeroxPaymentDetails zeroxPaymentDetails = zeroxPaymentMapper(thirdPartyPaymentDetails);
		mtemplate.save(zeroxPaymentDetails);
		return "SUCCESS";
	}

	private ZeroxPaymentDetails zeroxPaymentMapper(ThirdPartyPaymentDetails thirdPartyPaymentDetails) {
		// TODO Auto-generated method stub

		ZeroxPaymentDetails zeroxPaymentDetails = new ZeroxPaymentDetails();
		// process Payment and extract various details of balance
		float mockBalance = 50;
		zeroxPaymentDetails.setCustId(thirdPartyPaymentDetails.getCustId());
		zeroxPaymentDetails.setMobileNo(thirdPartyPaymentDetails.getMobileNo());
		zeroxPaymentDetails.setEmailId(thirdPartyPaymentDetails.getEmailId());
		zeroxPaymentDetails.setZeroxBalance(mockBalance);
		zeroxPaymentDetails.setThirdPartyPaymentDetails(thirdPartyPaymentDetails);
		return zeroxPaymentDetails;
	}

	@Override
	public List<PaymentDetailsForMongoDB> orderHistory(String emailId) {
		// call Printrequest collections by CustId take 20 records
		/*
		 * 
		 */
		try {
			Query query = new Query(new Criteria().where("emailId").is(emailId));
			query.fields().include("orderId").include("emailId").include("amount").include("transType")
					.include("status").include("transactionData.error_code").include("createDate").include("transStatus");
			query.limit(serviceConfig.getRecordLimit());
			// To-Do acs by createOn
			//query.with(Sort.by(Order.asc("createDate")));
			query.with(new Sort(Sort.Direction.ASC, "createDate"));
			return mtemplate.find(query, PaymentDetailsForMongoDB.class);
			// call PaymentDetails by custId take 20 records

			// mapping and showing details.
		} catch (Exception e) {
			logger.error(" Failed to fetch error", e);
			throw new NoRecordFoundException("Query Failed to execute");
		}

	}

	@Override
	public boolean validateOwnerOfWallet(ZeroxPaymentDetails wallet) {

		// To-Do Token Validation
		// UserId and and token should be match
		// Exit in Redis
		return true;

		// return false;
	}

	@Override
	public ZeroxPaymentDetails getWalletBalance(ZeroxPaymentDetails wallet) {
		// TODO Auto-generated method stub
		Query query = new Query(Criteria.where("emailId").is(wallet.getEmailId())
				.andOperator(Criteria.where("mobileNo").is(wallet.getMobileNo())));
		Customer customer = mtemplate.findOne(query, Customer.class);
		if (customer == null) {
			throw new NoRecordFoundException("Invalid Details");
		}
		Query walletquery = new Query(Criteria.where("emailId").is(wallet.getEmailId())
				.andOperator(Criteria.where("mobileNo").is(wallet.getMobileNo()))
				.andOperator(Criteria.where("custId").is(customer.getCustId())));

		query.fields().include("emailId").include("mobileNo").include("zeroxBalance");
		wallet = mtemplate.findOne(query, ZeroxPaymentDetails.class);

		return wallet;
	}

	@Override
	public boolean acceptPayment(PaymentDetailsForMongoDB paymentDetails) {
		// Persist the Razor Pay transaction response as it in 
		// TODO Auto-generated method stub
		// FIXME Transaction accept from app should be validate against RazorPay
		// There is Potential threat of tempering amount
		
		try {
				// FIXME
			// Here OrderId and TransactionId is always same.
			paymentDetails.setOrderId(paymentDetails.getTransId());
			// check Transaction Status
			logger.info(" Get Payment Details ======== {}", paymentDetails.toString());
			if (paymentDetails.getTransStatus()
					.equalsIgnoreCase(TransConstants.TransactionStatus.SUCCESS.name())) {
				
				logger.info(" Wallet Update Details ======");
				paymentDetails.setTransType(TransType.CREDIT.name());
				paymentDetails.setAmount(paymentDetails.getAmount());
				paymentDetails.setCreateDate(System.currentTimeMillis());
				try {
					WalletModel wallet = customerService.getWalletDetails(paymentDetails.getEmailId());
					
					if(wallet == null) {
						logger.error("Wallet ID Can not be Null");
						//FIXME
						// persist failure payment in DB
						throw new PaymentFailureException(" Wallet Does not exist with this EmailId", ErrorCodeNo.AMOUNTLOCK.getValue());
					}
					logger.info(" Wallet Details ========={}",wallet.toString());
					if (wallet.getWid().equals(paymentDetails.getWid())) {
						float newBalance = wallet.getBalance()
								+ paymentDetails.getAmount();
						Update update = new Update();
						update.set("balance", newBalance);
						Criteria emailcrit = Criteria.where("emailId").is(paymentDetails.getEmailId());
						Criteria widcrit = Criteria.where("wid").is(paymentDetails.getWid());
						Criteria crit = new Criteria().andOperator(emailcrit, widcrit);

						Query query = new Query(crit);
						mtemplate.updateFirst(query, update, WalletModel.class);
						logger.info(" Payment Detail Update Successfull");
					}
				} catch (Exception e) {
					logger.error("Failed to update Wallet Details", e);
					// FIXME
					// rollback or Add in Failure Data
					rollbackPayment(paymentDetails);
				}

			} else {
				paymentDetails.setTransStatus(paymentDetails.getTransStatus());
				// FIXME
				// Rollback and Refund calls
			}
			// Add Data in Wallet
			// Fetch wallet by WaletId and EmailId

			mtemplate.save(paymentDetails);

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Failed to execute Transaction", e);
			// FIXME
			// Persit in Failure and Rollback Logic
			// To-DO
			// failureRecoveryPayment();
			return false;
		}
		return true;
	}

	private void rollbackPayment(PaymentDetailsForMongoDB paymentDetails) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<com.first.test.model.PaymentDetails> orderHistoryPayment(String emailId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WalletModel checkbalance(String emailId, String walletId) {
		// TODO Auto-generated method stub
		WalletModel wallet = null;
		try {
			// FIXME
			// extract EmailId and walletID and validate them
			Query query = new Query(
					Criteria.where("emailId").is(emailId).andOperator(Criteria.where("wid").is(walletId)));
			query.fields().include("wid").include("emailId").include("balance");
			wallet = mtemplate.findOne(query, WalletModel.class);
			if (wallet == null) {
				logger.error(" Incorrect Wallet Details");
				throw new NoRecordFoundException("Incorrect Wallet Details");
			}
		} catch (Exception e) {
			logger.error(" Incorrect Wallet Details", e);
			throw new NoRecordFoundException("Incorrect Wallet Details");// TODO: handle exception
		}

		return wallet;
	}

	@Override
	public PrintDocRequest updateWalletDetail(PrintDocRequest printDocRequest) {
		
		// TODO Auto-generated method stub
		Query query = new Query(Criteria.where("emailId").is(printDocRequest.getEmailId()));
		WalletModel wallet = mtemplate.findOne(query, WalletModel.class);
		if (wallet == null) {
			logger.error(" Invalid Wallet. Invalid Print Request ");
			throw new PrintRequestValidationException("Wallet Does not Exist ", ErrorCodeNo.TemperedReuqest.getValue());
		}
		Update update = new Update();
		if (wallet.getBalance() - printDocRequest.getPrintCost() < 0) {
			throw new PrintRequestValidationException(" Insufficient Balance ", ErrorCodeNo.TemperedReuqest.getValue());
		}
		
		float loackamout = wallet.getLockamount() + (float)printDocRequest.getPrintCost();
		float effectiveBalance = wallet.getBalance() - wallet.getLockamount();
		if(effectiveBalance <0 ||  effectiveBalance <(float)printDocRequest.getPrintCost() ) {
			throw new PaymentFailureException(" Wait to complete Request. wallet Amount is locked", ErrorCodeNo.AMOUNTLOCK.getValue() );
		}
		
		try {
		//Payment audit purpose
		updatePaymentDetailsForHistory(printDocRequest, wallet, null);
		
		// payment locking
		update.set("lockamount",loackamout );
		mtemplate.updateFirst(query, update, WalletModel.class);
		printDocRequest.setPaymentStatus(PaymentStatus.PENDING.name());
		printDocRequest.setBalanceAmountToPay(0);
		
		//create privateSecure String
		//FIXME Secret Key should be Machine Specific
		//AESUtils.setKey(printDocRequest.getMachineId());
		//FIXME Secret Key should be Machine specific and should be fecth from DB/Redis
		// pass Secure key instead of printDocRequest.getMachineId()
		 printDocRequest.setPrintSecurity( aesUtils.encrypt(CommonUtility.createPrivateCertKey(printDocRequest), printDocRequest.getMachineId()));
		
		}catch (Exception e) {
			//  revert 
			updatePaymentDetailsForHistory(printDocRequest, wallet, PaymentStatus.FAIL.name());
			// FIXME Publish in Kafka to Process failed request
			logger.error("Failed Payment Processing. Rejected Request", e);
		}
		return printDocRequest;
	}

	//@Async
	@Transactional
	private void updatePaymentDetailsForHistory(PrintDocRequest printDocRequest, WalletModel wallet, String status) {
		
		PaymentDetailsForMongoDB paymentMongoDB = new PaymentDetailsForMongoDB();
		paymentMongoDB.setOrderId(printDocRequest.getPrintJobId());
		paymentMongoDB.setTransId(printDocRequest.getPrintJobId());
		paymentMongoDB.setEmailId(printDocRequest.getEmailId());
		paymentMongoDB.setWid(wallet.getWid());
		paymentMongoDB.setAmount((float) printDocRequest.getPrintCost());
		paymentMongoDB.setTransType(TransType.DEBIT.name());
		if(status == null) {
		paymentMongoDB.setTransStatus(PaymentStatus.COMPLETED.name());
		}else {
			paymentMongoDB.setTransStatus(status);
		}
		paymentMongoDB.setMobileNo(wallet.getMobileNo());
		paymentMongoDB.setCreateDate(printDocRequest.getCreateOn());
		OrderDetails order = new OrderDetails();
		order.setOrderId(printDocRequest.getPrintJobId());
		//paymentMongoDB.setOrderData(order);
		
		try {
		// FIXME In case of One user keep on using same trasaction
		// check if transaction already Exist 
		mtemplate.save(paymentMongoDB);
		}catch (Exception e) {
			logger.error("==== Transaction already Processed ========", e);
		}
		Query query = new Query(Criteria.where("printJobId").is(printDocRequest.getPaymentStatus()));
		Update update = new Update();
		update.set("status", PaymentStatus.COMPLETED.name());
		mtemplate.updateFirst(query, update, PrintCostPaymentRequest.class);
		
	}
	
	@Override
	public BulkPrintOrder executePaymentForBulk(BulkPrintOrder bulkOrder) {

		// To-DO Tax Calculation

		// Validate the Coupons and Get Coupon amount;
		float couponValue = offerService.getValidatePromocodeAndValue(bulkOrder.getEmailId(),
				bulkOrder.getCouponCode());
		logger.info("Offer Process : Coupon " + bulkOrder.getCouponCode() + " and Value " + couponValue);
		
		// get print cost
		// TODO Auto-generated method stub
		Query query = new Query(Criteria.where("emailId").is(bulkOrder.getEmailId()));
		WalletModel wallet = mtemplate.findOne(query, WalletModel.class);
		if (wallet == null) {
			logger.error(" Invalid Wallet. Invalid Print Request ");
			throw new PrintRequestValidationException("Wallet Does not Exist ", ErrorCodeNo.TemperedReuqest.getValue());
		}
		Update update = new Update();
		if (wallet.getBalance() - bulkOrder.getBuldOrderCost().getOrderCost() < 0) {
			throw new PrintRequestValidationException(" Insufficient Balance ", ErrorCodeNo.TemperedReuqest.getValue());
		}
				
		update.set("balance", wallet.getBalance() - bulkOrder.getBuldOrderCost().getOrderCost());
		mtemplate.updateFirst(query, update, WalletModel.class);
		bulkOrder.setPaymentStatus(PaymentStatus.COMPLETED.name());
		

	

		// FIXME If Partner Printer for Shared Printer
		//Payment audit purpose
		PaymentDetailsForMongoDB paymentMongoDB = new PaymentDetailsForMongoDB();
		
		try {
		logger.info("===== Payment bulk order ==={}", bulkOrder.toString());
		paymentMongoDB.setTransId(bulkOrder.getOrderId());
		paymentMongoDB.setOrderId(bulkOrder.getOrderId());
		paymentMongoDB.setEmailId(bulkOrder.getEmailId());
		paymentMongoDB.setWid(wallet.getWid());
		paymentMongoDB.setAmount((float) bulkOrder.getBuldOrderCost().getOrderCost());
		paymentMongoDB.setTransType(TransType.DEBIT.name());
		paymentMongoDB.setTransStatus(PaymentStatus.COMPLETED.name());
		paymentMongoDB.setMobileNo(wallet.getMobileNo());
		paymentMongoDB.setCreateDate(bulkOrder.getCreateDate());
		logger.info(" Persist Payment Details {}", paymentMongoDB.toString());
	    // FIXME
		 mtemplate.save(paymentMongoDB);
		}catch (Exception e) {
			logger.error(" Save Payment details {}",paymentMongoDB.toString(), e);
		}
		/*Query query = new Query(Criteria.where("printJobId").is(printDocRequest.getPaymentStatus()));
		Update update = new Update();
		update.set("status", PaymentStatus.COMPLETED.name());
		mtemplate.updateFirst(query, update, PrintCostPaymentRequest.class);*/
		return bulkOrder;

	}

	// FIXME Header and EmailId must be same. put validation
	@Override
	@Transactional
	public String executePaymentDeducted(PrintCostPaymentRequest printCostPaymentRequest, String emailId) {
		/*
		 1. fetch Details from printJobID
		 */
		
		 if(printCostPaymentRequest == null || !printCostPaymentRequest.getStatus().equalsIgnoreCase(PrintStatus.NEW.name())) {
			 
			 throw new NoRecordFoundException("Print Job does not exist");
		 }
		  
		 if((int)printCostPaymentRequest.getTotalCost() == 0 || (int)printCostPaymentRequest.getTotalCost() < 0 ) {
			 throw new InvalidDataException("Request tempered", 1101);
		 }
		
		logger.info("=========="+emailId+"============="+printCostPaymentRequest.getEmailId());
		 if(!emailId.equalsIgnoreCase(printCostPaymentRequest.getEmailId())) {
			 throw new InvalidDataException("Request tempered EmailId does not match", 1102);
		 }
		 // fetch details of Wallet 
		 Query query1 = new Query(Criteria.where("emailId").is(emailId));
		 WalletModel wallet = mtemplate.findOne(query1, WalletModel.class);
		 
		 if(wallet == null) {
			 throw new InvalidDataException("Request tempered EmailId does not match", 1103);
		 }
		 float newLockAmount = wallet.getLockamount() + (float) printCostPaymentRequest.getTotalCost();
		 float newUpdateAmount = wallet.getBalance() - (float) printCostPaymentRequest.getTotalCost();
		 
		 if(newLockAmount> wallet.getBalance()) {
			 throw new InvalidDataException("Request tempered EmailId does not match", 1103);
		 }
		 wallet.setBalance(newUpdateAmount);
		 Update update = new Update();
		 update.set("lockamount", newLockAmount);
		 mtemplate.updateFirst(query1, update, WalletModel.class);
		
		 /*PaymentDetailsForMongoDB paymentDetails = new PaymentDetailsForMongoDB();
		 paymentDetails.setWid(wallet.getWid());
		 paymentDetails.setEmailId(emailId);
		 paymentDetails.setAmount((float) printCostPaymentRequest.getTotalCost());
		 paymentDetails.setTransType(TransType.DEBIT.name());
		 paymentDetails.setCreateDate(System.currentTimeMillis());
         paymentDetails.setOrderId(printCostPaymentRequest.getPrintJobId());
         paymentDetails.setTransId(printCostPaymentRequest.getPrintJobId());
         paymentDetails.setTransStatus(PaymentStatus.PENDING.name());
		 mtemplate.save(paymentDetails) ;*/
		 
		 Query queryUpdate = new Query(Criteria.where("printJobId").is(printCostPaymentRequest.getPrintJobId()));
		 Update updateJob = new Update();
		 updateJob.set("status", ServiceStatus.PROCESSING.name());
		 mtemplate.updateFirst(queryUpdate, updateJob, PrintCostPaymentRequest.class);
		 
		 /*
		 2. deduct amount from wallet
		 3. 
		 orderId;
		 amount
		 emailId
		 status
		 ackBy
		
		 refundStatus
		 createDate
		  
		 */
		return printCostPaymentRequest.getPrintJobId();
	}

	@Override
	@Transactional
	public String finalPaymentDeductedFromMachine(PrintDocRequest printDocRequest) {
		/*
		 1. fetch Details from printJobID
		 */
		
		 // fetch details of Wallet 
		 Query query1 = new Query(Criteria.where("emailId").is(printDocRequest.getEmailId()));
		 WalletModel wallet = mtemplate.findOne(query1, WalletModel.class);
		 
		 if(wallet == null) {
			 throw new InvalidDataException("Request tempered EmailId does not match", 1103);
		 }
		 
		 float newLockAmount = wallet.getLockamount() - (float) printDocRequest.getPrintCost();
			
		 float newUpdateAmount = wallet.getBalance() - (float) printDocRequest.getPrintCost();
		
		 if(newUpdateAmount<0) {
			 throw new InvalidDataException("Request tempered EmailId does not match", 1103);
		 }
		 wallet.setBalance(newUpdateAmount);
		 
		 Update update = new Update();
		 update.set("balance", newUpdateAmount);
		 update.set("lockamount", newLockAmount);
		 
		 mtemplate.updateFirst(query1, update, WalletModel.class);
		
		 Query queryUpdate = new Query(Criteria.where("printJobId").is(printDocRequest.getPrintJobId()));
		 Update updatePayment = new Update();
		 updatePayment.set("transStatus", PaymentStatus.COMPLETED.name());
		 mtemplate.updateFirst(queryUpdate, updatePayment, PaymentDetailsForMongoDB.class) ;
		 
		 
		 Update updateJob = new Update();
		 updateJob.set("status", ServiceStatus.SUCCESS.name());
		 mtemplate.updateFirst(queryUpdate, updateJob, PrintCostPaymentRequest.class);
		
		return printDocRequest.getPrintJobId();
	}

}
