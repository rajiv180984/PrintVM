package com.first.hivemqtt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.first.machin.service.PrintServiceHandlerService;

import om.first.machin.model.PartnerItemRequest;
import om.first.machin.model.PrintDocRequest;

//@Component
public class HiveMQTTSubscriber implements CommandLineRunner {
	
	@Autowired
	PrintServiceHandlerService printServiceHandlerService;
	
	@Value("${hivemqtt.cloud.url}")
	private String hivemqttCloudURL;
	
	
	
	public void mqttsubscriber() {
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(" This is Subscriber testing ");
		MqttClient client = null;
		//client.connect(options);
		try {
			// local hiveqtt url
			 client = new MqttClient(hivemqttCloudURL, // URI
					MqttClient.generateClientId(), // ClientId
					new MemoryPersistence());
			MqttConnectOptions option = new MqttConnectOptions();
			
			option.setCleanSession(false);
			option.setKeepAliveInterval(180);
		
			// option.setUserName("admin");
			// option.setPassword("hivemq".toCharArray());

			client.setCallback(new MqttCallback() {

				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					System.out.println("============ " + message.toString());
					if (message != null) {
						System.out.println("============ " + message.toString());
						Map<String,Object> requestType = getTypeOfRequest(message);
						System.out.println(" Type of Object "+ requestType);
						
						// assigned into  Queue
						
						//PrintDocRequest printDocRequest = objectMapper.readValue(message.getPayload(), PrintDocRequest.class);
					//	System.out.println("========Request ======== "+ printDocRequest.toString());
					//printServiceHandlerService.printProcessRecord(printDocRequest);
					}

				}

				private HashMap<String, Object> getTypeOfRequest(MqttMessage message) {
					try {
						
						ObjectMapper objectMapper = new ObjectMapper();
						HashMap<String, Object> map = new HashMap<String, Object>();
						HashMap<String, Object> returnMap = new HashMap<String, Object>();
						map = objectMapper.readValue(message.getPayload(), map.getClass());
						System.out.println("======Request ====== "+map.get("requestType"));
						returnMap.put("requestType",  map.get("requestType").toString());
						
						PartnerItemRequest partner = null;
						PrintDocRequest printDocRequest = null;
						
						if( map.get("requestType").toString().equals("PARTNER")) {
							
							 partner = objectMapper.readValue(message.getPayload(), PartnerItemRequest.class );
							 returnMap.put("object",  partner);
						}else if( map.get("requestType").toString().equals("DIRECT")){
							printDocRequest = objectMapper.readValue(message.getPayload(), PrintDocRequest.class );
							 returnMap.put("object",  printDocRequest);
						}else if( map.get("requestType").toString().equals("REMOTE")) {
							
						}else if( map.get("requestType").toString().equals("CLOUD")) {
							
						}
						
					} catch (Exception e) {
						/*try {
							PartnerItemRequest printDocRequest = objectMapper.readValue(message.getPayload(), PartnerItemRequest.class);
							return "PARTNERPRINT";
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}*/
						e.printStackTrace();
					}
					return null;
				}

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					// TODO Auto-generated method stub

				}

				@Override
				public void connectionLost(Throwable cause) {
					// TODO Auto-generated method stub
					System.out.println("Connection to MQTT is lost....");
					// Try to restablish connection
				}
			});

			client.connect(option);
			client.subscribe("zeroxTopic/8782");
			
			// client.publish("test", new MqttMessage(payload.getBytes()));
			// client.subscribe("");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Persistence
	

	}

	@Override
	public void run(String... args) throws Exception {
		mqttsubscriber();

	}
}
