package com.first.test.util;

public enum ErrorCodeNo {
	// DB Level Exception Custom Error Code will start 100
	// Service Level Code start 500
	// API level code start from 300
	// other Exception or erro will 400
  NoRecord(100), DUPLICATE_DATA(101), NOTEXIST(404),
  MicroServiceUnavailable(510), UnableToProcess(511),
  InsufficientParams(410), validationFailed(411), 
  //Customer Profile issue 
  ProfileFailed(601), DUPLICATE_PROFILE(602), DEACTIVATED(603),
  DUPLICATE_EMAIL(604), DUPLICATE_MOBILE(605),INVALID_MOBILE(613),
  
  InsufficientPriviledge(701),
  
  TemperedReuqest(801),PRINTJOB_FAILED(802),
  AMOUNTLOCK(901),
	
	// Print JOB Specific code
	ALREADYPROCESSED(701);
	
  
  
  private final int value;

	ErrorCodeNo(final int newValue) {
      value = newValue;
  }

  public int getValue() { return value; }
}
