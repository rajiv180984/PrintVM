package com.first.test.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.first.test.doa.MachineDao;

@Component
public class AdminServiceQuartz {

	@Autowired
	MachineDao machineDoa;
	// Send Notification to care Taker on empty tray
	// Case 1: ON Empty Paper Tray Printer Programming
	
	// Evaluate what are tray are empty
	@Scheduled(fixedDelay = 15*60*1000)
	public void emtryTrayNotification() {
		
		machineDoa.paperRefilNotification(); 
		
		
		
	}
	
	// Case 2: Based on time when tray was filled and Till now total prints
	
	// ink Capacity and Total Paper Printed from time It was filled
	
	// Case 3: On Stuck of Paper or Machine Issue
	

}
