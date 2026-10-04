package com.first.test.doa;

import com.first.test.model.CostDerivative;
import com.first.test.model.PrintCostPaymentRequest;
import com.first.test.model.PrintDocRequest;
import com.first.test.model.PrintJobStatusUpdate;

public interface CustomerPrintDao {

	PrintDocRequest persist(PrintDocRequest rintDocRequest);
	
	CostDerivative getCostDerivativeByMachineId(PrintCostPaymentRequest printCostPaymentRequest);

	void updatePrintJobstatus(PrintJobStatusUpdate printJobStatusUpdate);

	void upsertPrintDocRequestAfterPayment(PrintDocRequest printRequest);

	void addCostDerivatives(CostDerivative costDerivative);

	String getCustomerIdByPhoneAndEmail(String mobileNo);

}
