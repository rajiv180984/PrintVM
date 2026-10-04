package com.first.test.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.first.test.model.MachineCareTaker;
import com.first.test.model.MachineDetails;
import com.first.test.model.WIFIMachine;
import com.first.test.service.MachineService;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/first/machine")
public class MachineController {

	// add machine details
	private static Logger logger = LoggerFactory.getLogger(MachineController.class);


	@Autowired
	MachineService machineService;
	
	@PostMapping(value = "/registerMachine")
	public ResponseEntity<MachineDetails> addMachineDetail(@RequestBody @Validated MachineDetails machineDetails) {
		
		machineService.addMachineDetail( machineDetails);
		return new ResponseEntity<MachineDetails>(machineDetails, HttpStatus.CREATED);
	}

	// add QR Code and Machine Association
	@PostMapping(value = "/qrcode")
	public ResponseEntity<WIFIMachine>  addQRCodeForMachine(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "machineJson", required = true) String machineJson){
		
		Gson gson = new Gson();
		WIFIMachine wifi = gson.fromJson(machineJson, WIFIMachine.class);
		System.out.println(" ======= Machine :"+ wifi.toString());
		machineService.saveQRCodeDetails(wifi);
        // persist encrypted 
		// encryption
		
		return new ResponseEntity<WIFIMachine>(new WIFIMachine(), HttpStatus.OK);
	}
	
	// add care-taker details

	@PostMapping(value="/registerCareTaker")
	public ResponseEntity<MachineCareTaker> registerCareTaker(@RequestBody MachineCareTaker machineCareTaker){
		machineService.registerCareTaker(machineCareTaker);
		return new ResponseEntity<MachineCareTaker>(machineCareTaker, HttpStatus.CREATED);
	}

	//add careTaker and machine details
	@PutMapping(value="/machineCaretakermapping/{machineId}")
	public ResponseEntity<String> addCaretakerMachine(@PathVariable(name="machineId") String machineId,
			@RequestParam(value="careTakerId") String careTakerId){
		System.out.println(" Machine and Care Taker Controller");
		machineService.addCaretakerMachine(machineId, careTakerId);
		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
	}
	
	// Update machine status [Active/suspend/delete]
	// due to some issue machine can be suspend
	@PutMapping(value="/updateMachineStatus/{machineId}")
	public ResponseEntity<String> updateMachineStatus(@PathVariable(name="machineId") String machineId,
			@RequestParam(value="machineStatus") String machineStatus){
		
		machineService.updateMachineStatus(machineId, machineStatus);
		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
	}

	// Notification to care taker
	@PostMapping("/machinenotification/{machineId}")
	public ResponseEntity<String> notifyToCareTaker(@PathVariable("machineId") String machineId, @RequestParam("issueName") String issueName){
		//
		machineService.machineIssueNotifier(machineId,issueName);
		return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
	}
	
	@GetMapping("/listOfMachineByContributorEmailId")
	ResponseEntity<List<MachineDetails>> listOfMachineByContributorEmailId(@RequestParam("emailIdOrMobile") String emailIdOrMobile){
		
		List<MachineDetails> machineList = machineService.listOfMachineByContributorEmailId(emailIdOrMobile);
		return new ResponseEntity<List<MachineDetails>>(machineList, HttpStatus.OK);
	}
	
}
