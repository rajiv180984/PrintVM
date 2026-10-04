package com.first.machin.machinezerox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import com.first.machin.service.PrintServiceHandlerService;

import om.first.machin.model.PrintDocRequest;

import java.lang.reflect.Type;

/**
 * This class is an implementation for <code>StompSessionHandlerAdapter</code>.
 * Once a connection is established, We subscribe to /topic/messages and 
 * send a sample message to server.
 * 
 * @author Kalyan
 *
 */
@Component
public class ZeroxStompSessionHandler extends StompSessionHandlerAdapter {

    private Logger logger = LogManager.getLogger(ZeroxStompSessionHandler.class);

    @Autowired
    PrintServiceHandlerService printServiceHandlerService;
    
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/pushNotification", this);
        logger.info("Subscribed to /topic/pushNotification");
        /*session.send("/topic/pushNotification", getSampleMessage());
        logger.info("Message sent to websocket server");*/
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return PrintDocRequest.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
    	PrintDocRequest printRequest = (PrintDocRequest) payload;
        System.out.println("=================== "+printRequest.toString());
        logger.info("Received : " + printRequest.toString());
        
        printServiceHandlerService.printProcessRecord(printRequest);
        //initiate download 
        // if direct emmediatly download
        
        // if offline the async download 
        
        
        
    }

    /**
     * A sample message instance.
     * @return instance of <code>Message</code>
     */
    private Message getSampleMessage() {
        Message msg = new Message();
        msg.setFirstName("Nicky");
        msg.setLastName("Howdy!!");
        return msg;
    }
}