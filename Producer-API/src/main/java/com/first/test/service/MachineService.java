package com.first.test.service;

import java.util.List;

import com.first.test.model.MachineCareTaker;
import com.first.test.model.MachineDetails;
import com.first.test.model.WIFIMachine;

public interface MachineService {

	void addMachineDetail(MachineDetails machineDetails);

	void registerCareTaker(MachineCareTaker machineCareTaker);

	void addCaretakerMachine(String machineId, String careTakerId);

	void updateMachineStatus(String machineId, String machineStatus);

	void machineIssueNotifier(String machineId, String issueName);

	List<MachineDetails> listOfMachineByContributorEmailId(String emailIdOrMobile);

	WIFIMachine getMachineSecurityInfo(String machineId);

	void saveQRCodeDetails(WIFIMachine wifi);
	
	String getMachineDeatail(String machineId);
 
	
	
}
