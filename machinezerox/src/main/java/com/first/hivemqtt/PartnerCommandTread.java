package com.first.hivemqtt;

import com.first.machin.service.PrintServiceHandlerService;

import om.first.machin.model.PartnerItemRequest;

public class PartnerCommandTread implements Runnable {

	private PrintServiceHandlerService printServiceHandlerService;
    private PartnerItemRequest partnerItemRequest;
    
	public PartnerCommandTread(PartnerItemRequest partnerItemRequest, PrintServiceHandlerService printServiceHandlerService) {
		this.partnerItemRequest = partnerItemRequest;
		this.printServiceHandlerService = printServiceHandlerService;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		printServiceHandlerService.createPrintJObFromPartnerCommand(partnerItemRequest);
	}

}
