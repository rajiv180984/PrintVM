package com.first.test.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.first.test.model.BulkPrintOrder;
import com.first.test.model.Factor2OTPResponse;
import com.first.test.model.GeoLocation;
import com.first.test.model.MachineDetails;
import com.first.test.model.PublicDocDetails;
import com.first.test.model.ShareDocForMachineByProfessionals;
import com.first.test.model.WIFIMachine;
import com.first.test.model.ZeroxPaymentDetails;

public interface AdminService {
	String addPublicDocument(MultipartFile file, PublicDocDetails docDetailsJson);

	PublicDocDetails publicdoctomachines(MultipartFile file, PublicDocDetails publicDocDetails);

	List<MachineDetails> getAllNearbyPrinter(GeoLocation geoLocation);

	List<MachineDetails> getAllPrinterByAreaName(String name);

	List<PublicDocDetails> getPublicDocByCategory(String category);

	List<PublicDocDetails> getPublicDocSpecificToMachine(String machineId);

	List<MachineDetails> searchMachinesByGeoLocation(GeoLocation geoLocation);

	List<String> getAllCategory();
	
	public List<String> getAllCategoryByMachineId(String machineId);

	ZeroxPaymentDetails testAddWalletBalance(ZeroxPaymentDetails zeroxPaymentDetails);

	List<ShareDocForMachineByProfessionals> getDocListByCategory(String category, String machineId);

	List<PublicDocDetails> docSearch(String srcText);

	BulkPrintOrder buldOrder(MultipartFile file, MultipartFile coverPage, BulkPrintOrder bulkOrder);
	
	// Validate Coupons
	
	double bulkPrintPricing(BulkPrintOrder bulkOrder);

	List<ShareDocForMachineByProfessionals> getDocListByCategoryAndMachineId(String category, String machineId);

	WIFIMachine getWIFIMachineDetails(WIFIMachine wifiMachine, String printjobId, String emailId);

	BulkPrintOrder placeOrder(BulkPrintOrder bulkOrder);

	BulkPrintOrder orderDetailsByOrderId(String orderId);

	List<BulkPrintOrder> listOfPendingOrders(String emailId);

	Factor2OTPResponse generateOTP(String mobileNo);

	void updateMachineIP(String machineId, String ipaddress);
		
	
	
	

}
