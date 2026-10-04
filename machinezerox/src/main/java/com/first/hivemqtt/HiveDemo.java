package com.first.hivemqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class HiveDemo {
	public static void main(String[] args) {
		System.out.println(" This is Hive Testing ");
		try {
			MqttClient client = new MqttClient( 
				    "tcp://0.0.0.0:1883", //URI 
				    MqttClient.generateClientId(), //ClientId 
				    new MemoryPersistence());
			String payload = new String("its is awesom");
			MqttConnectOptions option = new MqttConnectOptions();
			option.setCleanSession(false);
			option.setUserName("admin");
			option.setPassword("hivemq".toCharArray());
			
			
			/*client.subscribe("test");
			client.setCallback(new MqttCallback() {
				
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					if(message !=null ) {
						System.out.println("============ "+ message.toString());
					}
					
				}
				
				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void connectionLost(Throwable cause) {
					// TODO Auto-generated method stub
					
				}
			});*/
			client.connect(option);
			client.publish("test", payload.getBytes(),2, true);
			//client.publish("test", new MqttMessage(payload.getBytes()));
			//client.subscribe("");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //Persistence

	}
}
