package com.first.test.doa;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.first.test.controller.SmartOfficeServiceController;
import com.first.test.exception.ResourceNotFoundException;
import com.first.test.model.MachineCareTaker;
import com.first.test.model.MachineDetails;
import com.first.test.model.MachinePaperInkCountforRefile;
import com.first.test.model.PrintJobStatusUpdate;
import com.first.test.model.PrinterDetails;
import com.first.test.service.MachineService;
import com.mongodb.client.result.UpdateResult;

@Service
public class MachineDaoImpl implements MachineDao {

	private Logger logger = LoggerFactory.getLogger(MachineDaoImpl.class); 
	@Autowired
	MongoTemplate mongoTemplate;

	/*@PersistenceContext
	private EntityManager entityManager;
*/
	@Autowired
	MachineService machineServiceImpl;
	@Override
	public void addMachineDetail(MachineDetails machineDetails) {
		
		mongoTemplate.save(machineDetails);

	}

	@Override
	public void registerCareTaker(MachineCareTaker machineCareTaker) {
		try {
		mongoTemplate.save(machineCareTaker);
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(" Failed to save ", e);
			
		}

	}

	@Override
	public void addCaretakerMachine(String machineId, String careTakerId) {
		// assign caretaker to Machine
		System.out.println(" Machine and Care Taker mapping "+careTakerId);
		Update update = new Update();
		update.set("careTakerId", careTakerId);

		Query query = new Query(new Criteria().where("machineId").is(machineId));
		mongoTemplate.updateFirst(query, update, MachineDetails.class);

		// one care-taker can and many machine

		Update updateCareTaker = new Update();
		query = new Query(new Criteria().where("caretakerId").is(careTakerId));
		MachineCareTaker machineCareTaker = mongoTemplate.findOne(query, MachineCareTaker.class);
		System.out.println("============= "+machineCareTaker);
		List<String> machineList = machineCareTaker.getListOfMachine();
		if (machineList != null) {
			machineList.add(machineId);
		} else {
			machineList = new ArrayList<>();
			machineList.add(machineId);
		}
		updateCareTaker.set("listOfMachine", machineList);
		mongoTemplate.updateFirst(query, updateCareTaker, MachineCareTaker.class);

	}

	@Override
	public void updateMachineStatus(String machineId, String machineStatus) {
		Update update = new Update();
		update.set("activeStatus", machineStatus);

		Query query = new Query(new Criteria().where("machineId").is(machineId));
		mongoTemplate.updateFirst(query, update, MachineDetails.class);

	}

	@Override
	public List<String> paperRefilNotification() {

		return null;
	}

	@Override
	public void updatePaperMachinePaperInkCountforRefile(PrintJobStatusUpdate printJobStatusUpdate) {
		

		System.out.println("======Update page COunt ========== ");
		MachinePaperInkCountforRefile machinePaperInkCountforRefile = mongoTemplate.findOne(
				new Query(new Criteria().where("machineId").is(printJobStatusUpdate.getMachineId())),
				MachinePaperInkCountforRefile.class);
		if(machinePaperInkCountforRefile == null) {
			logger.error(" NO Machine Details Available ");
			//throw new Exception();
			// This should be setup duening add new printer in cloud
			// To-Do Remove Statement 
			machinePaperInkCountforRefile = new MachinePaperInkCountforRefile();
			machinePaperInkCountforRefile.setPapercount(0);
			machinePaperInkCountforRefile.setPapercountForInk(0);
			machinePaperInkCountforRefile.setRefilIntThresold(5000);
			machinePaperInkCountforRefile.setRefilPaperThresold(500);
			machinePaperInkCountforRefile.setMachineId(printJobStatusUpdate.getMachineId());
			mongoTemplate.save(machinePaperInkCountforRefile);
		}
			
		logger.info("====== Saved========== "+ printJobStatusUpdate.toString() + "\n"+ machinePaperInkCountforRefile.toString());
		machinePaperInkCountforRefile
				.setPapercount(machinePaperInkCountforRefile.getPapercount() + printJobStatusUpdate.getPaperCount());
		machinePaperInkCountforRefile.setPapercountForInk(
				machinePaperInkCountforRefile.getPapercountForInk() + printJobStatusUpdate.getPaperCount());

		Update update = new Update();

		update.set("papercountForInk", machinePaperInkCountforRefile.getPapercountForInk());
		update.set("papercount", machinePaperInkCountforRefile.getPapercount());

		Query query = new Query(new Criteria().where("machineId").is(printJobStatusUpdate.getMachineId()));
		if (machinePaperInkCountforRefile == null) {
			mongoTemplate.save(machinePaperInkCountforRefile);
		} else {
			UpdateResult upResult = mongoTemplate.updateFirst(query, update, MachinePaperInkCountforRefile.class);
		}

		if(machinePaperInkCountforRefile.getPapercount()> machinePaperInkCountforRefile.getRefilPaperThresold()*.75) {
			// send notification to care taker
			System.out.println(" Notify Caretaker");
			String issueName = "Printer running short of INK. Refile INK ";
			// TO-DO issueNameBuilder(); 
			machineServiceImpl.machineIssueNotifier(printJobStatusUpdate.getMachineId(), issueName);
		}
		if(machinePaperInkCountforRefile.getPapercountForInk()> machinePaperInkCountforRefile.getRefilInkThresold()*.75) {
			// send notification to care taker
			
			System.out.println(" Notify Caretaker");
			String issueName = "Printer running short of Paper. Refile Paper Tray ";
			// TO-DO issueNameBuilder(); 
			machineServiceImpl.machineIssueNotifier(printJobStatusUpdate.getMachineId(), issueName);
		}
	}

	@Override
	public MachineCareTaker getCareTakerDetailsByMachineId(String machineId) {
		Query q = new Query(new Criteria().where("machineId").is(machineId));
		MachineDetails detail = mongoTemplate.findOne(q, MachineDetails.class);
		
		Query q2 = new Query(new Criteria().where("caretakerId").is(detail.getCareTakerId()));
		return mongoTemplate.findOne(q2, MachineCareTaker.class);
		
	}

	@Override
	public PrinterDetails getPrinterDetailsByName(String machineId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persistMachineIssueData(String machineId, String caretakerId, String issueName) {
		// TODO Auto-generated method stub
		// take action to set the staus of machine also

	}

	@Override
	public String checkCareTaker(String careTakerId) {
		System.out.println("Carretaker Id "+ careTakerId);
		try {
		Query query = new Query(new Criteria().where("caretakerId").is(careTakerId));
		MachineCareTaker machineCareTaker = mongoTemplate.findOne(query, MachineCareTaker.class);
		if(machineCareTaker !=null) {
			return machineCareTaker.getCaretakerId();
		}else {
			throw new ResourceNotFoundException(" Care Taker can not be Null");
		}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error(" failed to fetch the care taker",e);
		}
		return null;
	}

}
