package com.first.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.first.test.exception.NoRecordFoundException;
import com.first.test.model.AutherityRoles;
import com.first.test.model.Customer;

@Service
public class ValidationServiceImpl implements ValidationService{

	@Autowired
	MongoTemplate mtemplate;
	
	@Override
	public boolean checkPrivildge(String ownerEmailId, String ownerMobileNo) {
		
		Query query = new Query(Criteria.where("emailId").is(ownerEmailId));
		Customer customer = mtemplate.findOne(query, Customer.class);
		if(customer == null) {
			throw new NoRecordFoundException(ownerEmailId+ " No Customer exist");
			
		}
		if(customer.getRole().equals(AutherityRoles.ROLE_USER.name())||customer.getRole().equals(AutherityRoles.ROLE_GUEST.name())) {
			return false;
		}
			
		return true;
	}

}
