package com.first.hivemqtt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.machin.service.PrintServiceHandlerService;
import com.first.machin.utils.RequestType;

import om.first.machin.model.Domain;
import om.first.machin.model.PartnerItemRequest;
import om.first.machin.model.PrintDocRequest;

@Component
public class CustomeQueue extends ArrayList {
	
	@Autowired
	PrintServiceHandlerService printServiceHandlerService;

	private static final String OBJECT = "object";
	private static final String REQUEST_TYPE = "requestType";
	// ToDo List should be priority queue
	ExecutorService executor;
	private List<String> list;

	public CustomeQueue() {
		super();
		this.list = new ArrayList<>();
		this.executor = Executors.newFixedThreadPool(20);
	}

	@Override
	public boolean add(Object e) {
		try{
			list.add(e.toString());
		
		// On Add Object inside list
		System.out.println("I added in Quque");
		HashMap<String, Object> map = getTypeOfRequest(e.toString());
		
		System.out.println("Ready To create thread ");
		System.out.println(" Map "+ map.toString());
		System.out.println("\n "+ map.get(REQUEST_TYPE));
		System.out.println("\n"+map.get(REQUEST_TYPE).equals(RequestType.DIRECT.name()));
		
		
		Thread zeroxThread = null;
		// This is applicable when Partner send Print Command
		if (map.get(REQUEST_TYPE).equals(RequestType.PARTNER.name())) {
			System.out.println("This is Partner Print ");
			PartnerItemRequest partnerItemRequest = (PartnerItemRequest)map.get(OBJECT);
			zeroxThread = new Thread(new PartnerCommandTread(partnerItemRequest, printServiceHandlerService));
			zeroxThread.setPriority(9);
			// User Send request for Print
		} else if (map.get(REQUEST_TYPE).equals(RequestType.DIRECT.name())) {
			System.out.println( " I am Direct Thread ");
			PrintDocRequest printDocRequest = (PrintDocRequest)map.get(OBJECT);
			System.out.println("=======Print ========= "+printDocRequest.toString());
			zeroxThread = new Thread(new PrintJobThread(printDocRequest, printServiceHandlerService));
			
			zeroxThread.setPriority(10);
			// User send Print Request from Remote User/Partner will send Print command when user in front of machine
		} else if (map.get(REQUEST_TYPE).equals(RequestType.REMOTE.name())) {
			System.out.println( " I am Remote Thread "+ map.toString());
			PrintDocRequest printDocRequest = (PrintDocRequest)map.get(OBJECT);
			System.out.println("=======Print ========= "+printDocRequest.toString());
			zeroxThread = new Thread(new PrintRequestDownloandThread(printDocRequest, printServiceHandlerService ));
			zeroxThread.setPriority(5);
			//zerosfxThread.setName("Download");
			
			// PRINT_LATER means User Send Print from Remote and When He is in front of machine can give command when payment is done.
		} else if (map.get(REQUEST_TYPE).equals(RequestType.PRINT_LATER.name())) {
			System.out.println("This is Print Later");
			PartnerItemRequest partnerItemRequest = (PartnerItemRequest)map.get(OBJECT);
			zeroxThread = new Thread(new PartnerCommandTread(partnerItemRequest, printServiceHandlerService));
			
			zeroxThread.setPriority(8);
		}
		System.out.println("THread ready to execute ");
		if (zeroxThread != null) {
			System.out.println("THread ready to execute submit");
			Future<?> future = executor.submit(zeroxThread);
			
		}
		}catch (Exception e1) {
			e1.printStackTrace();
		}
		return false;

	}

	private HashMap<String, Object> getTypeOfRequest(String message) {
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		try {

			System.out.println(" getTypeOfRequest ========:  "+ message);
			ObjectMapper objectMapper = new ObjectMapper();
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			map = objectMapper.readValue(message, map.getClass());
			System.out.println("======Request ====== " + map.get(REQUEST_TYPE));
			returnMap.put(REQUEST_TYPE, map.get(REQUEST_TYPE).toString());

			PartnerItemRequest partner = null;
			PrintDocRequest printDocRequest = null;

			if (map.get(REQUEST_TYPE).toString().equals("PARTNER")) {

				partner = objectMapper.readValue(message, PartnerItemRequest.class);
				System.out.println("It is parsed");
				
				returnMap.put(OBJECT, partner);
			}else if (map.get(REQUEST_TYPE).toString().equals("PRINT_LATER")) {

				partner = objectMapper.readValue(message, PartnerItemRequest.class);
				System.out.println("It is parsed");
				
				returnMap.put(OBJECT, partner);
			}else if (map.get(REQUEST_TYPE).toString().equals("DIRECT")) {
				System.out.println("Parsing of print Request +++++==");
				printDocRequest = objectMapper.readValue(message, PrintDocRequest.class);
				System.out.println("Parsing of print Request done");
				returnMap.put(OBJECT, printDocRequest);
			} else if (map.get(REQUEST_TYPE).toString().equals("REMOTE")) {
				System.out.println("Parsing of print Request +++++==");
				printDocRequest = objectMapper.readValue(message, PrintDocRequest.class);
				System.out.println("Parsing of print Request done");
				returnMap.put(OBJECT, printDocRequest);
			} else if (map.get(REQUEST_TYPE).toString().equals("CLOUD")) {

			}else {
				System.out.println("Failed ");
			}
System.out.println("Completed");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return returnMap;
	}

}
