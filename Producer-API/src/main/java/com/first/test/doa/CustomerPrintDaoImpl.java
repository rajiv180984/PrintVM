package com.first.test.doa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.first.test.model.CostDerivative;
import com.first.test.model.Customer;
import com.first.test.model.PrintCostPaymentRequest;
import com.first.test.model.PrintDocRequest;
import com.first.test.model.PrintJobStatusUpdate;
import com.first.test.service.UserPrintDocServiceImpl;
import com.first.test.util.CommonUtility;
import com.first.test.util.PrintStatus;
import com.first.test.util.RefundStatus;
import com.first.test.util.ZeroxConstants;
import com.mongodb.client.result.UpdateResult;

@Service
public class CustomerPrintDaoImpl implements CustomerPrintDao{
	private static Logger logger = LoggerFactory.getLogger(CustomerPrintDaoImpl.class);
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public PrintDocRequest persist(PrintDocRequest printDocRequest) {
		try {
			printDocRequest.setOrderId(printDocRequest.getPrintJobId());
			logger.info(" Save Print Request {}", printDocRequest);
			mongoTemplate.save(printDocRequest);
		} catch (Exception e) {
			logger.error("Failed to persit ", e);
		}
		return printDocRequest;
	}

	@Override
	public CostDerivative getCostDerivativeByMachineId(PrintCostPaymentRequest printCostPaymentRequest) {
		// TODO Auto-generated method stub
		if (printCostPaymentRequest.getCityName() == null || printCostPaymentRequest.getCityName().isEmpty()) {
			printCostPaymentRequest.setCityName("Lucknow");
		}
		Query query = new Query();
		Criteria machineCrit = Criteria.where("machineId").is(printCostPaymentRequest.getMachineId());
		Criteria cityCrit = Criteria.where("cityName").is(printCostPaymentRequest.getCityName());
		Criteria crit = new Criteria();
		crit.orOperator(cityCrit, machineCrit);
		query.addCriteria(crit);

		CostDerivative costDerivative = mongoTemplate.findOne(query, CostDerivative.class);
		if (costDerivative == null) {
			costDerivative = new CostDerivative();
			costDerivative.setCostPerPage(CommonUtility.getRangeCost(printCostPaymentRequest.getNumberOfPage()));
		}
		return costDerivative;
	}

	@Override
	public void updatePrintJobstatus(PrintJobStatusUpdate printJobStatusUpdate) {
		
		Update update = new Update();
		update.set("printStatus", printJobStatusUpdate.getPrintJobStatus());
		if (!printJobStatusUpdate.getPrintJobStatus().equalsIgnoreCase(PrintStatus.COMPLETE.name())) {
			update.set("causeOfFailure", printJobStatusUpdate.getCauseOfFailure());
			
		}
		if(printJobStatusUpdate.getRefundInitiate().equals(RefundStatus.INIT)) {
			
		}
		Query query = new Query(new Criteria().where("printJobId").is(printJobStatusUpdate.getPrintJobId()));
		UpdateResult  upResult = mongoTemplate.updateFirst(query, update, PrintDocRequest.class);
		
	
	}

	@Override
	@Transactional
	public void upsertPrintDocRequestAfterPayment(PrintDocRequest printRequest) {
		
		Query query = new Query(new Criteria().where("printJobId").is(printRequest.getPrintJobId()));
		
		// update Payment details
		// save the Thirdparty Payment details
	    Update update = new Update();
	    update.set("paymentStatus", ZeroxConstants.COMPLETE );
	    update.set("balanceAmountToPay", printRequest.getBalanceAmountToPay());
	   // printRequest.getPrintCost();
	    //update.set("thirdPartyPaymentDetails", printRequest.getThirdPartyPaymentDetails());
		mongoTemplate.updateMulti(query, update, PrintDocRequest.class);
		// update Payment details 
		//mongoTemplate.save(printRequest.getThirdPartyPaymentDetails());
		
	}

	@Override
	public void addCostDerivatives(CostDerivative costDerivative) {
		mongoTemplate.save(costDerivative);
		
	}

	@Override
	public String getCustomerIdByPhoneAndEmail(String mobileNo) {
		Query query = new Query(new Criteria("mobileNo").is(mobileNo));
		Customer customer = mongoTemplate.findOne(query, Customer.class);
		if(customer == null) {
			return "GUEST";
		}
		return customer.getCustId();
	}

	
	

}
