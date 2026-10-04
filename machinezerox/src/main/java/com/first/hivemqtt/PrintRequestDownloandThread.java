package com.first.hivemqtt;

import com.first.machin.service.PrintServiceHandlerService;

import om.first.machin.model.PrintDocRequest;

public class PrintRequestDownloandThread implements Runnable {

	private PrintDocRequest printDocRequest;
	private PrintServiceHandlerService printServiceHandlerService;

	public PrintRequestDownloandThread(PrintDocRequest printDocRequest,
			PrintServiceHandlerService printServiceHandlerService) {
		this.printDocRequest = printDocRequest;
		this.printServiceHandlerService = printServiceHandlerService;

	}

	private void processRemoteRequestRecord(PrintDocRequest printDocRequest,
			PrintServiceHandlerService printServiceHandlerService) {
		
		printServiceHandlerService.remotePrintPersist(printDocRequest);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		processRemoteRequestRecord(printDocRequest, printServiceHandlerService);
	}

}
