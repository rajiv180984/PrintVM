package com.first.hivemqtt;

import com.first.machin.service.PrintServiceHandlerService;

import om.first.machin.model.PrintDocRequest;

// This Thread for Printing 
public class PrintJobThread implements Runnable{

	private PrintDocRequest printDocRequest;
	
	private PrintServiceHandlerService printServiceHandlerService;
	
	
	public PrintJobThread(PrintDocRequest printDocRequest, PrintServiceHandlerService printServiceHandlerService) {
		super();
		this.printDocRequest = printDocRequest;
		this.printServiceHandlerService =  printServiceHandlerService;
		
		
	}


	private void processRecords(PrintDocRequest printDocRequest) {
		// TODO Auto-generated method stub
		System.out.println("Persist records and download file locally");
		
		String result = printServiceHandlerService.printProcessRecord(printDocRequest);
		System.out.println(" Result :"+result);
		
		
	}

	@Override
	public void run() {
	 System.out.println(" ==== print Job===== "+ printDocRequest);
	 // check type 
	 processRecords(printDocRequest);
	 
		
	}


}
