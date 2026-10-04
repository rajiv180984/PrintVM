package com.first.machin.machinezerox;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

//@Component
public class ConfigCommandLineRunner implements CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger(ConfigCommandLineRunner.class);

	@Autowired
	ZeroxStompSessionHandler zeroxStompSessionHandler;
	private static String URL = "ws://localhost:8080/websocket";
	@Override
	public void run(String... args) throws Exception {
		logger.info("ConfigCommandLineRunner Execution Started");
		WebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
		
		List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport( new StandardWebSocketClient()) );
		WebSocketClient transport = new SockJsClient(transports);
		//WebSocketStompClient stompClient = new WebSocketStompClient(transport);
		
		/*List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport(simpleWebSocketClient));*/
		SockJsClient sockJsClient = new SockJsClient(transports);
		WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
		System.out.println("We are ready ");
		

	        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

	        StompSessionHandler zeroxSessionHandler =zeroxStompSessionHandler; // new ZeroxStompSessionHandler();
	        
	      // StompSessionHandler sessionHandler = new MyStompSessionHandler();
	       //stompClient.connect(URL, sessionHandler);
	      
	       // To Dot for testing 
	       stompClient.connect(URL, zeroxSessionHandler);

	        new Scanner(System.in).nextLine(); // Don't close immediately.
	}

}
