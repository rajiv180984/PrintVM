package com.first.test.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.first.test.config.ProducerKafkaConfig;
import com.first.test.exception.InvalidDataException;
import com.first.test.model.MachineDetails;
import com.first.test.model.PartnerDetails;
import com.first.test.model.PartnerItemRequest;
import com.first.test.model.PrintDocRequest;
import com.first.test.producer.ZeroxProducer;

@Service
public class PartnerServiceImpl implements PartnerService {

	@Autowired
	MongoTemplate mongotemplate;
	
	@Autowired
	ZeroxProducer producer;
	
	@Autowired
	ProducerKafkaConfig producerKafkaConfig;
	
	@Override
	public  List<PrintDocRequest> getListOfAllItemByPartnerId(String partnerId, String machineId) {
		
		//getMachineIdByPartnerId()
		String partnerIdDetails = getPartnerDetails(machineId);
		if(!partnerId.equals(partnerIdDetails)) {
			throw new InvalidDataException(" Partner Id does  not match with Machine ID");
		}
		// get all details belongs to machine belongs to partner and will send print request to printer
		
		// To-Do : Add Pagination
		Query query = new Query( new Criteria("machineId").is(machineId).andOperator(new Criteria("printStatus").is("COMPLETED")));
		query.with(new Sort(Sort.Direction.ASC,"createOn"));
		query.fields().exclude("documentURL").exclude("thirdPartyPaymentDetails");
		List<PrintDocRequest> docList = mongotemplate.find(query, PrintDocRequest.class);
		// TODO Auto-generated method stub
		
		// fetch all items by deviceId with status pending.
		return docList;
		
	}

	private String getPartnerDetails(String machineId) {
		Query query = new Query(new Criteria("machineId").is(machineId));
		query.fields().include("printerId");
		
		MachineDetails machineDetals = mongotemplate.findOne(query, MachineDetails.class);
		
		return machineDetals.getPrinterId();
	}

	@Override
	public void publishPrintCommand(PartnerItemRequest partnerItemRequest) {
		// MobileNo and Code both lying on Partner DashBoard
		// when mobile and Security validate at partner then Publish
		try {
			// Check Payment status If Payment done, Than Send request to machine with DocId

			// On Publish with doc Id, Doc will be print
			// on successfull print status will be update and partner dashboard It become
			// disabled

			producer.runProducerCustomeTopic(partnerItemRequest.getMachineId(), partnerItemRequest, producerKafkaConfig.getPartnerTopic());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public PartnerDetails getPartnerId(String partnerId) {
		Query query = new Query(new Criteria().where("partnerId").is(partnerId));
		PartnerDetails partnerDetails = mongotemplate.findOne(query, PartnerDetails.class);
		
		return partnerDetails;
	}

	@Override
	public PartnerDetails registerPartner(PartnerDetails partnerDetails) {
		// TODO Auto-generated method stub
		partnerDetails.setPartnerId(partnerDetails.getPartnerMobileNo());
		mongotemplate.save(partnerDetails);
		return partnerDetails;
	}

}
