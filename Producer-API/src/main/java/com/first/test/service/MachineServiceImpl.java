package com.first.test.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.amazonaws.services.servermigration.model.UnauthorizedOperationException;
import com.first.test.controller.AdminController;
import com.first.test.doa.MachineDao;
import com.first.test.encrypt.AESUtils;
import com.first.test.exception.InvalidDataException;
import com.first.test.exception.NoRecordFoundException;
import com.first.test.model.BusinessType;
import com.first.test.model.ContrbutorMachines;
import com.first.test.model.MachineCareTaker;
import com.first.test.model.MachineDetails;
import com.first.test.model.PartnerDetails;
import com.first.test.model.PrinterDetails;
import com.first.test.model.WIFIMachine;
import com.first.test.notification.EmailServiceUtility;
import com.first.test.notification.SMSSendUtility;
import com.first.test.util.CommonUtility;
import com.first.test.util.MachineStatus;

@Service
public class MachineServiceImpl implements MachineService {

	@Autowired
	MachineDao machineDao;

	@Autowired
	PartnerService partnerService;

	@Autowired
	EmailServiceUtility emailServiceUtility;

	@Autowired
	SMSSendUtility smsSendUtility;

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Autowired
	AESUtils aesUtils;
	
	//FIXME
	// Replace by Radis
	public static Map<String, String> machineKeyMap = new HashMap<>();

	private static Logger logger = LoggerFactory.getLogger(MachineServiceImpl.class);

	
	@Override
	public void addMachineDetail(MachineDetails machineDetails) {
		// unique machine Id
		// check the care taker id
		System.out.println(" machine details " + machineDetails.toString());
		String careTakerId = machineDao.checkCareTaker(machineDetails.getCareTakerId());
		if (careTakerId == null) {
			throw new InvalidDataException("Machine can not register without caretaker");
		}
		machineDetails = validateBusinessType(machineDetails);
		machineDetails.setActiveStatus(MachineStatus.ACTIVE.name());
		machineDao.addMachineDetail(machineDetails);

	}

	private MachineDetails validateBusinessType(MachineDetails machineDetails) {
		if (machineDetails.getBusinessType() != null
				&& machineDetails.getBusinessType().equals(BusinessType.PARTNER.name())) {
			if (machineDetails.getPartnerId() == null) {
				throw new InvalidDataException("Partner is mendatory");
			}
			PartnerDetails partner = partnerService.getPartnerId(machineDetails.getPartnerId());
			if (partner == null) {
				throw new InvalidDataException("Partner is mendatory");
			}
		} else {
			machineDetails.setBusinessType(BusinessType.ZEROX.name());
		}
		return machineDetails;
	}

	@Override
	public void registerCareTaker(MachineCareTaker machineCareTaker) {
		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();
		machineCareTaker.setCaretakerId(machineCareTaker.getMobile());
		machineDao.registerCareTaker(machineCareTaker);
	}

	@Override
	public void addCaretakerMachine(String machineId, String careTakerId) {
		machineDao.addCaretakerMachine(machineId, careTakerId);

	}

	@Override
	public void updateMachineStatus(String machineId, String machineStatus) {
		machineDao.updateMachineStatus(machineId, machineStatus);

	}

	@Override
	public void machineIssueNotifier(String machineId, String issueName) {
		Map<String, String> emailMap = new HashMap<>();

		Map<String, String> smsMap = new HashMap<>();

		// TODO Auto-generated method stub
		// Get CareTaker Details

		// Get Printer Detail
		PrinterDetails printerDetails = machineDao.getPrinterDetailsByName(machineId);
		// Send mail and SMS to care taker

		MachineCareTaker machineCareTaker = machineDao.getCareTakerDetailsByMachineId(machineId);
		
		try {

			// EMail
			emailMap = emailBuilder(machineId, issueName, machineCareTaker, printerDetails);
			smsMap = smsBuilder(machineId, issueName, machineCareTaker, printerDetails);

			emailServiceUtility.sendMail(emailMap);
			smsSendUtility.sendSMSToCaretaker(smsMap);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// persist issue in db
		machineDao.persistMachineIssueData(machineId, machineCareTaker.getCaretakerId(), issueName);
	}

	private Map<String, String> smsBuilder(String machineId, String issueName, MachineCareTaker machineCareTaker,
			PrinterDetails printerDetails) {
		Map<String, String> smsMap = new HashMap<>();
		smsMap.put("mobile1", machineCareTaker.getMobile());
		smsMap.put("mobile2", machineCareTaker.getMobile());

		return smsMap;
	}

	private Map<String, String> emailBuilder(String machineId, String issueName, MachineCareTaker machineCareTaker,
			PrinterDetails printerDetails) {
		Map<String, String> emailMap = new HashMap<>();
		String emailSubject = getEmailSubject(issueName);
		String emailText = getEmailContent(machineId, issueName, machineCareTaker.getName(), printerDetails);

		emailMap.put("subject", emailSubject);
		emailMap.put("emailtext", emailText);
		// To-Do Email Id of Admin
		emailMap.put("emailfrom", "rathor.rajeev@gmail.com");
		emailMap.put("emailto", machineCareTaker.getEmail());
		return emailMap;
	}

	private String getEmailContent(String machineId, String issueName, String name, PrinterDetails printerDetails) {
		// TODO Auto-generated method stub
		// formate email contents
		return "Machine in Trouble "+ machineId +" machine Issue "+ issueName;
	}

	private String getEmailSubject(String issueName) {
		// TODO Auto-generated method stub
		// Based on Analysis or Printer status create subject
		return issueName;
	}

	@Override
	public List<MachineDetails> listOfMachineByContributorEmailId(String emailIdOrMobile) {
		// TODO Auto-generated method stub 
		List<MachineDetails> machineDetailList = null;
		Query query = new Query( Criteria.where("emailId").is(emailIdOrMobile));
		
		ContrbutorMachines ContrbutorMachines =  mongoTemplate.findOne(query, ContrbutorMachines.class);
		if(ContrbutorMachines == null) {
			throw new UnauthorizedOperationException("No Machine Allocated");
		}
		Query queryMachine = new Query(Criteria.where("machineId").in(ContrbutorMachines.getMachineIds()));
		queryMachine.fields().include("machineId").include("machineAddress").include("geoLocation").include("machineName");
		machineDetailList = mongoTemplate.find(queryMachine, MachineDetails.class);
		if(machineDetailList == null) {
			throw new UnauthorizedOperationException("No Machine Allocated");
		}
		return machineDetailList;
	}

	@Override
	public WIFIMachine getMachineSecurityInfo(String machineId) {
		
		Query query = new Query(Criteria.where("machineId").is(machineId));
		WIFIMachine wifiMachine= mongoTemplate.findOne(query,  WIFIMachine.class);
		return wifiMachine;
		
	}

	@Override
	public void saveQRCodeDetails(WIFIMachine wifi) {
		
		
		if(getMachineDeatail(wifi.getMachineId() )== null) {
			throw new NoRecordFoundException("Machine Does not exist");
		}
		try {
		// TODO Auto-generated method stub
		String encrptedSSID = aesUtils.encryptMachine(wifi.getSsid(), wifi.getMachineKey());
		wifi.setEncryptedWifiSSID(encrptedSSID);
		wifi.setEncryptedWifiPassword(aesUtils.encryptMachine(wifi.getWifipwd(), wifi.getMachineKey()));
		mongoTemplate.save(wifi);
		
		// FIXME Replace with Radis code
		machineKeyMap.put(encrptedSSID, wifi.getMachineId());
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("Failed to persist QR Code", e);
		}

	}

	@Override
	public String getMachineDeatail(String machineId) {
		// FIXME Add in Radis 
		Query query = new Query(Criteria.where("machineId").is(machineId));
		MachineDetails machine = mongoTemplate.findOne(query, MachineDetails.class);
		
		return machine.getMachineId();
	}

}
