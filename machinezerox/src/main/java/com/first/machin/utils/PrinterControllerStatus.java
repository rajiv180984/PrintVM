package com.first.machin.utils;

import java.util.HashSet;
import java.util.Set;

import javax.print.attribute.standard.PrinterStateReason;

public class PrinterControllerStatus {

	private static Set<String> printerControllerStatus = new HashSet<String>();
	static {
		printerControllerStatus.add(PrinterStateReason.CONNECTING_TO_DEVICE.getName());
		printerControllerStatus.add(PrinterStateReason.MARKER_SUPPLY_EMPTY.getName());
		printerControllerStatus.add(PrinterStateReason.MARKER_SUPPLY_EMPTY.getName());
		
		// Tonner or INK
		printerControllerStatus.add(PrinterStateReason.TONER_EMPTY.getName());
		printerControllerStatus.add(PrinterStateReason.TONER_LOW.getName());
		
		// Tray empty
		printerControllerStatus.add(PrinterStateReason.INPUT_TRAY_MISSING.getName());
		printerControllerStatus.add(PrinterStateReason.MEDIA_EMPTY.getName());
		printerControllerStatus.add(PrinterStateReason.MEDIA_LOW.getName());
		printerControllerStatus.add(PrinterStateReason.MEDIA_NEEDED.getName());
		
		
		// Machine Jam
		printerControllerStatus.add(PrinterStateReason.MOVING_TO_PAUSED.getName());
		printerControllerStatus.add(PrinterStateReason.PAUSED.getName());
			
		// Printer Pause/STOP
		//STOPPING
		
		// Connectivity issue
	}
	
	public static boolean getPrinterReason(String printerReason) {
		return printerControllerStatus.contains(printerReason);
	}

}
