package com.first.hivemqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CustomeCallBack implements MqttCallback {

	@Autowired
	private CustomeQueue queue;
	
	

	public CustomeCallBack() {
		super();
		//this.queue = new CustomeQueue() ;
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		System.out.println("=====CallBack====== " + message.toString());
		try {
		if (message != null) {
			System.out.println("============ " + message.toString());
			queue.add(message.toString());
			
			// assigned into Queue

			// PrintDocRequest printDocRequest =
			// objectMapper.readValue(message.getPayload(), PrintDocRequest.class);
			// System.out.println("========Request ======== "+ printDocRequest.toString());
			// printServiceHandlerService.printProcessRecord(printDocRequest);
		}

		}catch (Exception e) {
		    e.printStackTrace();
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		System.out.println(" Delvery Of Message is complete");
	}

	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		System.out.println("Connection to MQTT is lost....");
		// Try to restablish connection
	}

}
