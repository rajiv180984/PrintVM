package com.first.hivemqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.machin.service.PrintServiceHandlerService;

@Component
public class HiveMQTTSubscriberWithThreading implements CommandLineRunner {
	
	@Autowired
	PrintServiceHandlerService printServiceHandlerService;
	
	@Value("${machineId}")
	private String machineId;
	

	@Value("${hivemqtt.cloud.url}")
	private String hivemqttCloudURL;
	
	@Autowired
	CustomeCallBack customeCallBack;
	
	public void mqttsubscriber() {
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(" This is Subscriber testing "+ hivemqttCloudURL);
		
		MqttClient client = null;
		//client.connect(options);
		try {
			//"tcp://0.0.0.0:1883"
			 client = new MqttClient(hivemqttCloudURL, // URI
					MqttClient.generateClientId(), // ClientId
					new MemoryPersistence());
			MqttConnectOptions option = new MqttConnectOptions();
			
			option.setCleanSession(false);
			option.setKeepAliveInterval(180);
		
			// option.setUserName("admin");
			// option.setPassword("hivemq".toCharArray());

			client.setCallback(customeCallBack);
			client.connect(option);
			String topicName = "zeroxTopic/".concat(machineId);
			System.out.println(" Topic Name "+ topicName);
			client.subscribe(topicName);
			
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
