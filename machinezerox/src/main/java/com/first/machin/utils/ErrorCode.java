package com.first.machin.utils;

public enum ErrorCode {
  
	// P --> means Printer Specific
	// M --> Machine Specific
	// T --> Transaction Specific
	
	T101("Already Executed Print"),
	T102("PrintJob Does not exist.Illegal Transacion"),
	
	M102("Machine is not ready to Process"),
	M103("Unable To Write File on Machine"),
	M104("FILE Corrupted"),
	
	P101("Unable to Print");
	;

	
	
	private final String errormsg;

	ErrorCode(final String newValue) {
		errormsg = newValue;
  }

  public String getErrorMSG() { return errormsg; }
}
