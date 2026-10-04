package com.first.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.first.test.doa.CustomerPrintDao;
import com.first.test.exception.IllegalOperationException;
import com.first.test.exception.InvalidDataException;
import com.first.test.exception.PaymentFailureException;
import com.first.test.model.Binding;
import com.first.test.model.CostDerivative;
import com.first.test.model.PrintCostPaymentRequest;
import com.first.test.model.WalletModel;
import com.first.test.util.CommonUtility;
import com.first.test.util.PageCountUtility;
import com.first.test.util.ServiceStatus;

@Service
public class CostDerivativeServiceImpl implements CostDerivativeService {

	private static Logger logger = LoggerFactory.getLogger(CostDerivativeServiceImpl.class);

	@Autowired
	CustomerPrintDao customerPrintDao;

	@Autowired
	CustomerProfileService customerService;

	@Autowired
	OfferService offerService;
	
	@Autowired
	MongoTemplate mtemplate;

	// Add Cost in Redis to avoid db calls
	@Override
	public PrintCostPaymentRequest evaluateCost(PrintCostPaymentRequest docCostEvaluatorDeviceRequest) {

		// get CostDerivative\
		int pageCount = PageCountUtility.getTotalCountOfPrintPage(docCostEvaluatorDeviceRequest.getPrintExprn(), null);
		if(docCostEvaluatorDeviceRequest.getNoOfCopy()<=0  ) {
			throw new InvalidDataException("Invalid Number of Copy");
		}
		docCostEvaluatorDeviceRequest.setNumberOfPage(pageCount*docCostEvaluatorDeviceRequest.getNoOfCopy());
		WalletModel wallet = customerService.getWalletDetails(docCostEvaluatorDeviceRequest.getEmailId());
		
		if (wallet == null) {
			throw new PaymentFailureException("Wallet does not exist or request manuputed");
		}

		if (docCostEvaluatorDeviceRequest.getNumberOfPage() == 0 || docCostEvaluatorDeviceRequest.getNumberOfPage() < 0) {
			throw new InvalidDataException("Page Print expn or Page count not be null");
		}

		try {
			// To-Do evaluate Machine ID by Lat and Langt
			String machineId = getMachineIdByLongLat(docCostEvaluatorDeviceRequest.getLat(),
					docCostEvaluatorDeviceRequest.getLongt());
			docCostEvaluatorDeviceRequest.setMachineId(machineId);
			CostDerivative costDerivative = customerPrintDao
					.getCostDerivativeByMachineId(docCostEvaluatorDeviceRequest);

			if (costDerivative == null) {
				costDerivative = new CostDerivative();
				logger.error(" Fail to find cost ");
				costDerivative.setCostPerPage(2.0f);
			}
			float totalCost = costDerivative.getCostPerPage() * docCostEvaluatorDeviceRequest.getNumberOfPage();

			if(totalCost <=0) {
				throw new IllegalOperationException(" Cost can not be less than zero or negative");
			}
			docCostEvaluatorDeviceRequest.setTotalCost(totalCost);

			// check the wallet balance by Email
			if (wallet.getBalance() >= totalCost) {
				// create printJobId and OrderID
				docCostEvaluatorDeviceRequest.setEnoughBalance("YES");
				String printJobId = CommonUtility.randomUUIDGenerator();
				docCostEvaluatorDeviceRequest.setPrintJobId(printJobId);
				docCostEvaluatorDeviceRequest.setOrderId(printJobId);
				docCostEvaluatorDeviceRequest.setStatus("NEW");
				mtemplate.save(docCostEvaluatorDeviceRequest);
				
			} else {
				
				docCostEvaluatorDeviceRequest.setEnoughBalance("NO");
				
			}
			//  create presigned URL
			
			// FIXME
			// persist request in DB and validate while final processing
			// Otherwise request may be manipulated.
			
			//FIXME  
			//Persist it in Redis 
			// Once Redis Integrated than Remove MongoDb persistence
		} catch (Exception e) {
			logger.error(" failed to fetch cost", e);
			// docCostEvaluatorDeviceRequest.setTotalCost(2 *
			// docCostEvaluatorDeviceRequest.getNumberOfPage());
			throw new PaymentFailureException("Payment Failure exception");
		}
		
		return docCostEvaluatorDeviceRequest;

	}

	private String getMachineIdByLongLat(double lat, double longt) {

		// TODO Auto-generated method stub
		// To-Do evaluate Machine ID by Lat and Langt
		return null;
	}

	/*
	 * @Override public CostDerivative getCostDerivativeByMachineId(String
	 * machineId) { // TODO Auto-generated method stub return
	 * customerPrintDao.getCostDerivativeByMachineId(machineId, null); }
	 */
	// To-Do add caching to avoid db calls
	/*
	 * @Override public double getCostDerivativeByMachineIdAndCity(String machineId,
	 * String cityName) { // TODO Auto-generated method stub try { CostDerivative
	 * cost = customerPrintDao.getCostDerivativeByMachineId(machineId, null);
	 * if(cost != null) { return cost.getCostPerPage(); } } catch (Exception e) {
	 * logger.error(" Failed to fetch Cost ", e);
	 * 
	 * } return 2.0; }
	 */
	@Override
	public void addCostDerivatives(CostDerivative costDerivative) {

		customerPrintDao.addCostDerivatives(costDerivative);
	}

	@Override
	public CostDerivative getCostDerivativeByMachineId(String machineId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getCostDerivativeByMachineIdAndCity(String machineId, String cityName) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float bulkPrintCost(int pageCount, String pincode, String cityName) {
		// FIXME 
		float perPageCost = findPerPageCost(pincode, cityName);
		return perPageCost * pageCount;
	}

	private float findPerPageCost(String pincode, String cityName) {
		// FIXME
		// TODO Auto-generated method stub
		return 1.10f;
	}

	@Override
	public float getBindingCost(Binding binding) {
		// FIXME
		// TODO Auto-generated method stub
		return 25.0f;
	}

}
