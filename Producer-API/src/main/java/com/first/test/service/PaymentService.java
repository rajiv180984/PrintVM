package com.first.test.service;

import java.util.List;

import com.first.test.model.BulkPrintOrder;
import com.first.test.model.PaymentDetails;
import com.first.test.model.PaymentDetailsForMongoDB;
import com.first.test.model.PrintCostPaymentRequest;
import com.first.test.model.PrintDocRequest;
import com.first.test.model.ThirdPartyPaymentDetails;
import com.first.test.model.WalletModel;
import com.first.test.model.ZeroxPaymentDetails;

public interface PaymentService {
  
	//
	String evaluateCostOfJob(String machineId, String userId, int numberOfPage, String copounCode);
	PrintDocRequest executePaymentOfJob(PrintDocRequest printDocRequest);
	
	String mockPaymentAPI(ThirdPartyPaymentDetails thirdPartyPaymentDetails);
	boolean validateOwnerOfWallet(ZeroxPaymentDetails wallet);
	ZeroxPaymentDetails getWalletBalance(ZeroxPaymentDetails wallet);
	
	public List<PaymentDetailsForMongoDB> orderHistory(String emailId);
	boolean acceptPayment(PaymentDetailsForMongoDB paymentDetails);
	List<PaymentDetails> orderHistoryPayment(String emailId);
	WalletModel checkbalance(String emailId, String walletId);
	PrintDocRequest updateWalletDetail(PrintDocRequest printDocRequest);
	
	BulkPrintOrder executePaymentForBulk(BulkPrintOrder bulkOrder);
	
	String executePaymentDeducted(PrintCostPaymentRequest printCostPaymentRequest, String emailId);
	
	public String finalPaymentDeductedFromMachine(PrintDocRequest printDocRequest);
	
	
	
}
