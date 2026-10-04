package com.first.test.doa;

import com.first.test.model.MachinePaperInkCountforRefile;

public class AdminDOA {

	// Cron Job Evaluate notitication and read from DB send
	
	/*@PersistenceContext
	private EntityManager entityManager;
*/
	// update count,
	//@Transactional
	public boolean addVendorHQBPID(MachinePaperInkCountforRefile vendor) {
		try {
			/*Session session = entityManager.unwrap(Session.class);
			
			List<String> machineIds= session.createNamedQuery("notifyPaperCareTaker", String.class).getResultList();
			*///join between MachinePaperIntCountforRefile and careTakerId get all list of careTakerIDs
			
			// Iterate over CareTaker and Machine details 
		
			
			
		} catch (Exception e) {
			
			return false;
		}
		
		return true;
	}
	
	
}
