package com.first.test.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.first.test.model.ContributorRoleRequest;
import com.first.test.model.Feedback;
import com.first.test.util.ServiceStatus;
import com.mysql.cj.log.Log;

@Service
public class SupportServiceImpl implements SupportService{
	private static Logger logger = LoggerFactory.getLogger(SupportServiceImpl.class);

	@Autowired
	MongoTemplate mtemplate;
	
	
	@Override
	public String feedBackSubmit(Feedback feedback) {
		try {
			mtemplate.save(feedback);
			return ServiceStatus.SUCCESS.name();
		}catch (Exception e) {
			logger.error("Failed to persist feedback", e);
		}
		return ServiceStatus.FAILED.name();
	}

	@Override
	public List<Feedback> getFeedbackListByEmailId(String emailId) {
		try {
			Query query = new Query(Criteria.where("emailId").is(emailId));
			query.with(new Sort(Sort.Direction.ASC, "createOn")).limit(20);
			
			return mtemplate.find(query, Feedback.class);
		}catch (Exception e) {
		    logger.error("Failed to fetch list ", e);
		}
		return new ArrayList<>();

	}

	@Override
	public String contributorRoleRequest(ContributorRoleRequest request) {
		try {
			mtemplate.save(request);
		}catch (Exception e) {
			logger.info(" Failed to take request");
			
		}
		return null;
	}

}
