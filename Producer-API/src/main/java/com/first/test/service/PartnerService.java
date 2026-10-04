package com.first.test.service;

import java.util.List;

import com.first.test.model.PartnerDetails;
import com.first.test.model.PartnerItemRequest;
import com.first.test.model.PrintDocRequest;

public interface PartnerService {

	public List<PrintDocRequest> getListOfAllItemByPartnerId(String partnerId, String deviceId);

	public void publishPrintCommand(PartnerItemRequest partnerItemRequest);

	public PartnerDetails getPartnerId(String partnerId);

	public PartnerDetails registerPartner(PartnerDetails partnerDetails);
	
}
