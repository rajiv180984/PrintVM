package com.first.test.service;

import com.first.test.model.Binding;
import com.first.test.model.CostDerivative;
import com.first.test.model.PrintCostPaymentRequest;

public interface CostDerivativeService {
	PrintCostPaymentRequest evaluateCost(PrintCostPaymentRequest docCostEvaluatorDeviceRequest);

	CostDerivative getCostDerivativeByMachineId(String machineId);

	void addCostDerivatives(CostDerivative costDerivative);

	double getCostDerivativeByMachineIdAndCity(String machineId, String cityName);
	
	float bulkPrintCost(int pageCount, String pincode, String city);

	float getBindingCost(Binding binding);
}
