package com.first.machin.print;

import java.util.HashSet;
import java.util.Set;

import javax.print.attribute.AttributeSet;
import javax.print.attribute.standard.PrinterState;
import javax.print.attribute.standard.PrinterStateReason;
import javax.print.event.PrintServiceAttributeEvent;
import javax.print.event.PrintServiceAttributeListener;

import org.springframework.stereotype.Service;

import com.first.machin.utils.PrinterControllerStatus;

@Service
public class ZeroxPrintServiceAttributeListener implements PrintServiceAttributeListener {

	Set<String> set = new HashSet<String>();
	
	
	public ZeroxPrintServiceAttributeListener() {
		super();
	}


	@Override
	public void attributeUpdate(PrintServiceAttributeEvent psae) {
		// TODO Auto-generated method stub
		System.out.println(" This is PrintStatus Attribute ====");
		AttributeSet attributes = psae.getPrintService().getAttributes();
		System.out.println(" This is Attrubyte Sets  ===");
		// To-DO remove this code for Admin 
		if(attributes.get(PrinterState.class) == null || attributes.get(PrinterStateReason.class) == null) {
			return;
		}
		String printerState = attributes.get(PrinterState.class).toString();
		String printerStateReason = attributes.get(PrinterStateReason.class).toString();
		System.out.println("printerState = " + printerState);

		// If your printer state returns STOPPED
		System.out.println("printerStateReason = " + printerStateReason);
		if (printerState.equals(PrinterState.STOPPED.toString())) {
			
			if(PrinterControllerStatus.getPrinterReason(printerStateReason)) {
				// Invoke Central Service to notify Admin and CareTaker
			}
			
		}else if(printerState.equals(PrinterState.UNKNOWN.toString())) {
			if(PrinterControllerStatus.getPrinterReason(printerStateReason)) {
				// Invoke Central Service to notify Admin and CareTaker
			}
		}
	}

	
}
