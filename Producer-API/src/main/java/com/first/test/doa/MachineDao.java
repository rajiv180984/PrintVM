package com.first.test.doa;

import java.util.List;

import com.first.test.model.MachineCareTaker;
import com.first.test.model.MachineDetails;
import com.first.test.model.PrintJobStatusUpdate;
import com.first.test.model.PrinterDetails;

public interface MachineDao {
  	
	void addMachineDetail(MachineDetails machineDetails);

	void registerCareTaker(MachineCareTaker machineCareTaker);

	void addCaretakerMachine(String machineId, String careTakerId);

	void updateMachineStatus(String machineId, String machineStatus);

	List<String> paperRefilNotification();
	
	
	public void updatePaperMachinePaperInkCountforRefile(PrintJobStatusUpdate printJobStatusUpdate);


	MachineCareTaker getCareTakerDetailsByMachineId(String machineId);
	PrinterDetails getPrinterDetailsByName(String machineId);

	void persistMachineIssueData(String machineId, String caretakerId, String issueName);

	String checkCareTaker(String careTakerId);
	
	
	
}
