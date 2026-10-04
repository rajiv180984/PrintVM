/*package com.first.hivemqtt;

import java.io.IOException;

import javax.json.Json;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.first.machin.service.PrintServiceHandlerService;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5ClientBuilder;

import om.first.machin.model.PartnerItemRequest;
import om.first.machin.model.PrintDocRequest;

@Component
public class HiveMQTT5ClientSubscriber implements CommandLineRunner {
	
	@Autowired
	PrintServiceHandlerService printServiceHandlerService;
	
	public void mqttsubscriber() {
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(" This is Subscriber testing ");
		Mqtt5ClientBuilder client =  Mqtt5Client.builder();
		client.automaticReconnect();
		client.
				//addDisconnectedListener(context -> {
            //context.getReconnector().reconnect(true).delay(2, TimeUnit.SECONDS);
        });
		try {
			 client = new MqttClient("tcp://0.0.0.0:1883", // URI
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
						String requestType = getTypeOfRequest(message);
						System.out.println(" Type of Object "+ requestType);
						PrintDocRequest printDocRequest = objectMapper.readValue(message.getPayload(), PrintDocRequest.class);
						printServiceHandlerService.printProcessRecord(printDocRequest);
					}

				}

				private String getTypeOfRequest(MqttMessage message) {
					try {
						
						ObjectNode objectNode = objectMapper.createObjectNode();
						String requetType = objectNode.get("requestType").asText();
						System.out.println("============ "+requetType);
						
						PrintDocRequest printDocRequest = objectMapper.readValue(message.getPayload(), PrintDocRequest.class);
						return "ZEROXPRINT";
					} catch (IOException e) {
						try {
							PartnerItemRequest printDocRequest = objectMapper.readValue(message.getPayload(), PartnerItemRequest.class);
							return "PARTNERPRINT";
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
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
*/